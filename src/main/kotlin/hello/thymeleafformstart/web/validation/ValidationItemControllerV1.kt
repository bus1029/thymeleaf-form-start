package hello.thymeleafformstart.web.validation

import hello.thymeleafformstart.domain.item.DeliveryCode
import hello.thymeleafformstart.domain.item.Item
import hello.thymeleafformstart.domain.item.ItemRepository
import hello.thymeleafformstart.domain.item.ItemType
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v1/items")
class ValidationItemControllerV1 constructor(private val itemRepository: ItemRepository) {
  var logger: Logger = LoggerFactory.getLogger(ValidationItemControllerV1::class.java)

  @GetMapping
  fun items(model: Model): String {
    val items = itemRepository.findAll()
    model.addAttribute("items", items)
    return "validation/v1/items"
  }

  @GetMapping("/{itemId}")
  fun item(@PathVariable itemId: Long, model: Model): String {
    val item = itemRepository.findById(itemId)
    model.addAttribute("item", item)
    return "validation/v1/item"
  }

  @GetMapping("/add")
  fun addForm(model: Model): String {
    model.addAttribute("item", Item())
    return "validation/v1/addForm"
  }

  @PostMapping("/add")
  fun addItem(@ModelAttribute item: Item, redirectAttributes: RedirectAttributes, model: Model): String {
    // 검증 오류 결과 보관
    val errors = mutableMapOf<String, String>()
    checkValidation(item, errors)
    // 검증에 실패하면 다시 입력 폼으로
    if (errors.isNotEmpty()) {
      logger.error("errors = {}", errors)
      model.addAttribute("errors", errors)
      return "validation/v1/addForm"
    }

    val savedItem = itemRepository.save(item)
    redirectAttributes.addAttribute("itemId", savedItem.id)
    redirectAttributes.addAttribute("status", true)
    return "redirect:/validation/v1/items/{itemId}"
  }

  private fun checkValidation(item: Item, errors: MutableMap<String, String>) {
    // 검증 로직
    if (!StringUtils.hasText(item.itemName)) {
      errors["itemName"] = "상품 이름은 필수입니다."
    }
    // price 의 기본값으로 -1 을 세팅했기 때문에 null 체크는 무시
    if (item.price < 1000 || item.price > 1000000) {
      errors["price"] = "가격은 1,000 ~ 1,000,000 까지 허용합니다."
    }

    if (item.quantity >= 9999) {
      errors["quantity"] = "수량은 최대 9,999 까지 허용합니다."
    }

    // 복합 룰 검증
    if (item.quantity * item.price < 10000) {
      errors["globalError"] = "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = ${item.quantity * item.price}"
    }
  }

  @GetMapping("/{itemId}/edit")
  fun editForm(@PathVariable itemId: Long, model: Model): String {
    val item = itemRepository.findById(itemId)
    model.addAttribute("item", item)
    return "validation/v1/editForm"
  }

  @PostMapping("/{itemId}/edit")
  fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
    itemRepository.update(itemId, item)
    return "redirect:/validation/v1/items/{itemId}"
  }
}