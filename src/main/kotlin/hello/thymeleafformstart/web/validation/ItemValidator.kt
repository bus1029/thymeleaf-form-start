package hello.thymeleafformstart.web.validation

import hello.thymeleafformstart.domain.item.Item
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class ItemValidator : Validator {
  override fun supports(clazz: Class<*>): Boolean {
    // item == clazz
    // item == subClass
    return Item::class.java.isAssignableFrom(clazz)
  }

  override fun validate(target: Any, errors: Errors) {
    val item = target as Item

    // 검증 로직
    if (!StringUtils.hasText(item.itemName)) {
      errors.rejectValue("itemName", "required")
    }

    // price 의 기본값으로 -1 을 세팅했기 때문에 null 체크는 무시
    if (item.price < 1000 || item.price > 1000000) {
      errors.rejectValue("price", "range", arrayOf(1000, 1000000), null)
    }

    if (item.quantity >= 9999) {
      errors.rejectValue("quantity", "max", arrayOf(9999), null)
    }

    // 복합 룰 검증
    if (item.quantity * item.price < 10000) {
      errors.reject("totalPriceMin", arrayOf(10000, item.quantity * item.price), null)
    }
  }
}