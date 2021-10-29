package hello.thymeleafformstart.domain.item

/**
 * FAST: 빠른 배송
 * NORMAL: 일반 배송
 * SLOW: 느린 배송
 */
data class DeliveryCode(val code: String = "", val displayName: String = "")
