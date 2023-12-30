package com.example.datnsd56.service.impl;

import com.example.datnsd56.controller.AccountNotFoundException;
import com.example.datnsd56.entity.*;
import com.example.datnsd56.repository.CartItemRepository;
import com.example.datnsd56.repository.CartRepository;
import com.example.datnsd56.service.AccountService;
import com.example.datnsd56.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.annotation.SessionScope;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@SessionScope
@Service
public class CartSeviceImpl implements CartService {

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private CartRepository cartRepository;
    Map<Integer, CartItem> maps = new HashMap<>();
    @Autowired
    private AccountService accountService;

    @Override
    public void add(CartItem item) {
        CartItem cartItem = maps.get(item.getProductDetails().getId());
        if (cartItem == null) {
            maps.put(item.getProductDetails().getId(), item);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
    }

    @Override
    public void remove(Integer id) {
        maps.remove(id);
    }

    @Override
    public void clear() {
        maps.clear();
    }

    @Override
    public int getCount() {
        return maps.values().size();
    }

    @Override
    public void add1(Cart cart) {
        cartRepository.save(cart);
    }

    @Override
    public CartItem update(Integer id, Integer quantity) {
        CartItem cartItem = maps.get(id);
        cartItem.setQuantity(quantity);
        return cartItem;
    }

    @Override
    public Collection<CartItem> getAllItem() {
        return maps.values();
    }

//    @Override
//    public List<ViewCart> getAllCartItem() {
//        return cartItemRepository.getAllCartItem();
//    }

    @Override
    public double getAmount() {
        return maps.values().stream()
                .mapToDouble(item -> {
                    int quantity = item.getQuantity();
                    BigDecimal sellPrice = item.getProductDetails().getSellPrice();
                    return BigDecimal.valueOf(quantity).multiply(sellPrice).doubleValue();
                })
                .sum();
    }

    @Override
    public Cart findByNguoiDungId(Integer id) {
        return cartRepository.findByAccountId(id);
    }

    @Override
    @Transactional
    public Cart addToCart(ProductDetails productDetail, Integer quantity, String name) {
        Optional<Account> accountOptional = accountService.finByName(name);

        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            Cart cart = account.getCart();

            if (cart == null) {
                cart = new Cart();
                cart.setAccountId(account);
                // Lưu cart ngay sau khi tạo mới
                cartRepository.save(cart);
            }

            Set<CartItem> cartItems = cart.getCartItems();
            CartItem cartItem = find(cartItems, productDetail.getId());

            if (cartItem == null) {
                cartItem = new CartItem();
                cartItem.setProductDetails(productDetail);
                cartItem.setCart(cart);
                cartItem.setQuantity(quantity);
                cartItem.setCreateDate(LocalDate.now());
                cartItem.setUpdateDate(LocalDate.now());
                cartItem.setPrice(productDetail.getSellPrice());
                cartItem.setStatus("0");
                cartItems.add(cartItem);

                // Lưu cartItem ngay sau khi thêm vào cartItems
                cartItemRepository.save(cartItem);
            } else {
                // Nếu cartItem đã tồn tại, cập nhật quantity
                cartItem.setQuantity(cartItem.getQuantity() + quantity);
            }

            BigDecimal totalPrice = totalPrice(cartItems);
            int totalItems = totalItem(cartItems);

            cart.setCartItems(cartItems);
            cart.setTotalPrice(totalPrice);
            cart.setTotalItems(totalItems);
            cart.setCreateDate(LocalDate.now());
            cart.setUpdateDate(LocalDate.now());
            cart.setStatus("0");

            // Lưu cart sau khi cập nhật
            return cartRepository.save(cart);
        } else {
            throw new RuntimeException("Account not found for name: " + name);
        }
    }



    @Override
    @Transactional
    public Cart updateCart(ProductDetails productDetail, Integer quantity, String name) {
        Optional<Account> account = accountService.finByName(name);
        Cart cart = account.get().getCart();
        Set<CartItem> cardItemList = cart.getCartItems();
        CartItem item = find(cardItemList, productDetail.getId());
        int itemQuantity = quantity;

        item.setQuantity(itemQuantity);
        cartItemRepository.save(item);
        cart.setCartItems(cardItemList);
        int totalItems = totalItem(cardItemList);
        BigDecimal totalPrice = totalPrice(cardItemList);
        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Cart removeFromCart(ProductDetails productDetail, String name) {
        Optional<Account> account = accountService.finByName(name);
        Cart cart = account.get().getCart();
        Set<CartItem> cardItemList = cart.getCartItems();
        CartItem item = find(cardItemList, productDetail.getId());
        cardItemList.remove(item);
        cartItemRepository.delete(item);
        int totalItems = totalItem(cardItemList);
        BigDecimal totalPrice = totalPrice(cardItemList);
        cart.setCartItems(cardItemList);
        cart.setTotalItems(totalItems);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }

    @Override
    public SessionCart addToCartSession(SessionCart sessionCart, ProductDetails productDetail, Integer quantity) {
        SessionCartItem cartItem = findInSession(sessionCart, productDetail.getId());
        if (sessionCart == null) {
            sessionCart = new SessionCart();
        }
        Set<SessionCartItem> cartDetailList = sessionCart.getCartItems();
        BigDecimal unitPrice = productDetail.getSellPrice();
        int itemQuantity = 0;
        if (cartDetailList == null) {
            cartDetailList = new HashSet<>();
            if (cartItem == null) {
                cartItem = new SessionCartItem();
                cartItem.setProductDetail(productDetail);
                cartItem.setCart(sessionCart);
                cartItem.setCreateDate(LocalDate.now());
                cartItem.setUpdateDate(LocalDate.now());
                cartItem.setQuantity(quantity);
                cartItem.setStatus("0");
                cartDetailList.add(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        } else {
            if (cartItem == null) {
                cartItem = new SessionCartItem();
                cartItem.setProductDetail(productDetail);
                cartItem.setCart(sessionCart);
                cartItem.setQuantity(quantity);
                cartItem.setPrice(unitPrice);
                cartItem.setCreateDate(LocalDate.now());
                cartItem.setUpdateDate(LocalDate.now());
                cartItem.setStatus("0");
                cartDetailList.add(cartItem);
            } else {
                itemQuantity = cartItem.getQuantity() + quantity;
                cartItem.setQuantity(itemQuantity);
            }
        }
        sessionCart.setCartItems(cartDetailList);

        BigDecimal totalPrice = totalPriceSession(cartDetailList);
        int totalItems = totalItemSession(cartDetailList);

        sessionCart.setTotalPrice(totalPrice);
        sessionCart.setTotalItems(totalItems);
        sessionCart.setCreateDate(LocalDate.now());
        sessionCart.setUpdateDate(LocalDate.now());
        sessionCart.setStatus("0");
        return sessionCart;
    }

    @Override
    public SessionCart updateCartSession(SessionCart sessionCart, ProductDetails productDetail, Integer quantity) {
        Set<SessionCartItem> cardItemList = sessionCart.getCartItems();
        SessionCartItem item = findInSession(sessionCart, productDetail.getId());
        int itemQuantity = quantity;
        int totalItems = totalItemSession(cardItemList);
        BigDecimal totalPrice = totalPriceSession(cardItemList);
        item.setQuantity(itemQuantity);
        sessionCart.setCartItems(cardItemList);
        sessionCart.setTotalPrice(totalPrice);
        sessionCart.setTotalItems(totalItems);
        return sessionCart;
    }

    @Override
    public SessionCart removeFromCartSession(SessionCart sessionCart, ProductDetails productDetail) {
        Set<SessionCartItem> cardItemList = sessionCart.getCartItems();
        SessionCartItem item = findInSession(sessionCart, productDetail.getId());
        cardItemList.remove(item);
        int totalItems = totalItemSession(cardItemList);
        BigDecimal totalPrice = totalPriceSession(cardItemList);
        sessionCart.setCartItems(cardItemList);
        sessionCart.setTotalItems(totalItems);
        sessionCart.setTotalPrice(totalPrice);
        return sessionCart;
    }

    @Override
    @Transactional
    public Cart combineCart(SessionCart sessionCart, String name) {
        Optional<Account> account = accountService.finByName(name);
        Cart cart = account.get().getCart();
        if (cart == null) {
            cart = new Cart();
        }
        Set<CartItem> cartDetails = cart.getCartItems();
        if (cartDetails == null) {
            cartDetails = new HashSet<>();
        }
        Set<CartItem> sessionCartDetails = convertCartItem(sessionCart.getCartItems(), cart);
        for (CartItem cartDetail : sessionCartDetails) {
            cartDetails.add(cartDetail);
            cartItemRepository.save(cartDetail);
        }
        BigDecimal totalPrice = totalPrice(cartDetails);
        int totalItems = totalItem(cartDetails);
        cart.setTotalItems(totalItems);
        cart.setCartItems(cartDetails);
        cart.setTotalPrice(totalPrice);
        cart.setAccountId(account.get());
        return cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCartById(Integer id) {
        Cart cart = cartRepository.findById(id).orElse(null);
        if (!ObjectUtils.isEmpty(cart) && !ObjectUtils.isEmpty(cart.getCartItems())) {
            cartItemRepository.deleteAll(cart.getCartItems());
        }
        cart.getCartItems().clear();
        cart.setTotalPrice(BigDecimal.ZERO);
        cart.setTotalItems(0);
        cart.setCreateDate(LocalDate.now());
        cart.setUpdateDate(LocalDate.now());
        cartRepository.save(cart);
    }

    private CartItem find(Set<CartItem> cartItems, Integer productDetailId) {
        if (cartItems == null) {
            return null;
        }
        CartItem cartItem = null;
        for (CartItem item : cartItems) {
            if (item.getProductDetails().getId() == productDetailId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private SessionCartItem findInSession(SessionCart sessionCart, Integer productDetailId) {
        if (sessionCart == null) {
            return null;
        }
        SessionCartItem cartItem = null;
        for (SessionCartItem item : sessionCart.getCartItems()) {
            if (item.getProductDetail().getId() == productDetailId) {
                cartItem = item;
            }
        }
        return cartItem;
    }

    private int totalItem(Set<CartItem> cartItemList) {
        int totalItem = 0;
        for (CartItem item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private BigDecimal totalPrice(Set<CartItem> cartItemList) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (CartItem item : cartItemList) {
            BigDecimal price = item.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal subTotal = price.multiply(quantity);
            totalPrice = totalPrice.add(subTotal);
        }
        return totalPrice;

    }

    private int totalItemSession(Set<SessionCartItem> cartItemList) {
        int totalItem = 0;
        for (SessionCartItem item : cartItemList) {
            totalItem += item.getQuantity();
        }
        return totalItem;
    }

    private BigDecimal totalPriceSession(Set<SessionCartItem> cartItemList) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (SessionCartItem item : cartItemList) {
            BigDecimal price = item.getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            BigDecimal subTotal = price.multiply(quantity);
            totalPrice = totalPrice.add(subTotal);
        }
        return totalPrice;

    }

    private Set<CartItem> convertCartItem(Set<SessionCartItem> sessionCartItems, Cart cart) {
        Set<CartItem> cartDetails = new HashSet<>();
        for (SessionCartItem sessionCartItem : sessionCartItems) {
            CartItem cartDetail = find(cart.getCartItems(), sessionCartItem.getProductDetail().getId());
            if (cartDetail != null) {
                if (cartDetail.getQuantity() < sessionCartItem.getQuantity()) {
                    cartDetail.setQuantity(sessionCartItem.getQuantity());
                    cartDetails.add(cartDetail);
                }
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setQuantity(sessionCartItem.getQuantity());
                cartItem.setPrice(sessionCartItem.getPrice());
                cartItem.setProductDetails(sessionCartItem.getProductDetail());
                cartItem.setCart(cart);
                cartDetails.add(cartItem);
            }
        }
        return cartDetails;
    }

    @Override
    public Cart getCart(String name) {
        Optional<Account> account = accountService.finByName(name);
        Cart cart = account.get().getCart();
        return cart;
    }
}
