package hello.thymeleafformstart.domain.validation

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.validation.DefaultMessageCodesResolver

class MessageCodesResolverTest {
  val codesResolver = DefaultMessageCodesResolver()

  @Test
  fun messageCodesResolverObject() {
    val resolveMessageCodes = codesResolver.resolveMessageCodes("required", "item")
    for (resolveMessageCode in resolveMessageCodes) {
      println("resolveMessageCode = $resolveMessageCode")
    }

    Assertions.assertThat(resolveMessageCodes).containsExactly("required.item", "required")
  }

  @Test
  fun messageCodesResolverField() {
    val resolveMessageCodes = codesResolver.resolveMessageCodes("required", "item", "itemName", String::class.java)
    for (resolveMessageCode in resolveMessageCodes) {
      println("resolveMessageCode = $resolveMessageCode")
    }
    // bindingResult.rejectValue("itemName", "required", null, false, messageCodes, null, null)
    Assertions.assertThat(resolveMessageCodes).containsExactly("required.item.itemName", "required.itemName", "required.java.lang.String", "required")
  }
}