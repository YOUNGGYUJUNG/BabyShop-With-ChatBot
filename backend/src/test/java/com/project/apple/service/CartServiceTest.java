/*
package com.project.apple.service;


import com.project.apple.domain.Cart;
import com.project.apple.dto.cart.CartItemDto;
import com.project.apple.dto.cart.request.CartItemAddRequest;
import com.project.apple.dto.cart.request.CartItemDeleteRequest;
import com.project.apple.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addToCart_shouldSaveCartItem() {
        // given
        CartItemAddRequest request = new CartItemAddRequest(1L, 2L);
        Cart cart = Cart.builder()
                .memberId(1L)
                .productId(1L)
                .qnt(2L)
                .build();

        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        // when
        Long savedCartId = cartService.addToCart(request);

        // then
        assertThat(savedCartId).isNotNull();
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    void getCartList_shouldReturnCartItems() {
        // given
        Cart cart1 = Cart.builder().id(1L).memberId(1L).productId(1L).qnt(2).build();
        Cart cart2 = Cart.builder().id(2L).memberId(1L).productId(2L).qnt(3).build();
        when(cartRepository.findByMemberId(anyString())).thenReturn(List.of(cart1, cart2));

        // when
        List<CartItemDto> cartList = cartService.getCartList();

        // then
        assertThat(cartList).hasSize(2);
        assertThat(cartList.get(0).getProductId()).isEqualTo(1L);
        assertThat(cartList.get(1).getProductId()).isEqualTo(2L);
        verify(cartRepository, times(1)).findByMemberId(anyString());
    }
*/
/*
    @Test
    void updateCartItemCount_shouldThrowExceptionForInvalidCount() {
        // given
        CartItemDto dto = new CartItemDto(1L, 1L, 0); // Invalid count

        // when/then
        assertThatThrownBy(() -> cartService.updateCartItemCount(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("최소 1개 이상 담아주세요.");
    }
*//*

    @Test
    void deleteCartItem_shouldDeleteCartItemById() {
        // given
        CartItemDeleteRequest request = new CartItemDeleteRequest(1L);

        doNothing().when(cartRepository).deleteById(anyLong());

        // when
        cartService.deleteCartItem(request);

        // then
        verify(cartRepository, times(1)).deleteById(1L);
    }
}
*/
