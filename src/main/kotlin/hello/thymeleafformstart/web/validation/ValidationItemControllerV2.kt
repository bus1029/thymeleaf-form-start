package hello.thymeleafformstart.web.validation

import hello.thymeleafformstart.domain.item.Item
import hello.thymeleafformstart.domain.item.ItemRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.util.StringUtils
import org.springframework.validation.BindingResult
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes

@Controller
@RequestMapping("/validation/v2/items")
class ValidationItemControllerV2 constructor(private val itemRepository: ItemRepository,
                                             private val itemValidator: ItemValidator) {
  var logger: Logger = LoggerFactory.getLogger(ValidationItemControllerV2::class.java)

  @InitBinder
  fun init(webDataBinder: WebDataBinder) {
    webDataBinder.addValidators(itemValidator)
  }

  @GetMapping
  fun items(model: Model): String {
    val items = itemRepository.findAll()
    model.addAttribute("items", items)
    return "validation/v2/items"
  }

  @GetMapping("/{itemId}")
  fun item(@PathVariable itemId: Long, model: Model): String {
    val item = itemRepository.findById(itemId)
    model.addAttribute("item", item)
    return "validation/v2/item"
  }

  @GetMapping("/add")
  fun addForm(model: Model): String {
    model.addAttribute("item", Item())
    return "validation/v2/addForm"
  }

  //  @PostMapping("/add")
  fun addItemV1(@ModelAttribute item: Item?, bindingResult: BindingResult, redirectAttributes: RedirectAttributes, model: Model): String {
    checkValidation(item, bindingResult)
    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      logger.error("errors = {}", bindingResult)
      return "validation/v2/addForm"
    }

    item?.let {
      val savedItem = itemRepository.save(item)
      redirectAttributes.addAttribute("itemId", savedItem.id)
      redirectAttributes.addAttribute("status", true)
    }

    return "redirect:/validation/v2/items/{itemId}"
  }

  //  @PostMapping("/add")
  fun addItemV2(@ModelAttribute item: Item?, bindingResult: BindingResult, redirectAttributes: RedirectAttributes, model: Model): String {
    checkValidation2(item, bindingResult)
    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      logger.error("errors = {}", bindingResult)
      return "validation/v2/addForm"
    }

    item?.let {
      val savedItem = itemRepository.save(item)
      redirectAttributes.addAttribute("itemId", savedItem.id)
      redirectAttributes.addAttribute("status", true)
    }

    return "redirect:/validation/v2/items/{itemId}"
  }

  //  @PostMapping("/add")
  fun addItemV3(@ModelAttribute item: Item?, bindingResult: BindingResult, redirectAttributes: RedirectAttributes, model: Model): String {
    logger.info("objectName={}", bindingResult.objectName)
    logger.info("target={}", bindingResult.target)

    checkValidation3(item, bindingResult)
    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      logger.error("errors = {}", bindingResult)
      return "validation/v2/addForm"
    }

    item?.let {
      val savedItem = itemRepository.save(item)
      redirectAttributes.addAttribute("itemId", savedItem.id)
      redirectAttributes.addAttribute("status", true)
    }

    return "redirect:/validation/v2/items/{itemId}"
  }

  //  @PostMapping("/add")
  fun addItemV4(@ModelAttribute item: Item?, bindingResult: BindingResult, redirectAttributes: RedirectAttributes, model: Model): String {
    logger.info("objectName={}", bindingResult.objectName)
    logger.info("target={}", bindingResult.target)

    checkValidation4(item, bindingResult)
    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      logger.error("errors = {}", bindingResult)
      return "validation/v2/addForm"
    }

    item?.let {
      val savedItem = itemRepository.save(item)
      redirectAttributes.addAttribute("itemId", savedItem.id)
      redirectAttributes.addAttribute("status", true)
    }

    return "redirect:/validation/v2/items/{itemId}"
  }

//  @PostMapping("/add")
  fun addItemV5(@ModelAttribute item: Item?, bindingResult: BindingResult, redirectAttributes: RedirectAttributes, model: Model): String {
    logger.info("objectName={}", bindingResult.objectName)
    logger.info("target={}", bindingResult.target)

    item?.let {
      itemValidator.validate(item, bindingResult);
    }
    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      logger.error("errors = {}", bindingResult)
      return "validation/v2/addForm"
    }

    item?.let {
      val savedItem = itemRepository.save(item)
      redirectAttributes.addAttribute("itemId", savedItem.id)
      redirectAttributes.addAttribute("status", true)
    }

    return "redirect:/validation/v2/items/{itemId}"
  }

  @PostMapping("/add")
  fun addItemV6(@Validated @ModelAttribute item: Item?, bindingResult: BindingResult, redirectAttributes: RedirectAttributes, model: Model): String {
    logger.info("objectName={}", bindingResult.objectName)
    logger.info("target={}", bindingResult.target)

    // 검증에 실패하면 다시 입력 폼으로
    if (bindingResult.hasErrors()) {
      logger.error("errors = {}", bindingResult)
      return "validation/v2/addForm"
    }

    item?.let {
      val savedItem = itemRepository.save(item)
      redirectAttributes.addAttribute("itemId", savedItem.id)
      redirectAttributes.addAttribute("status", true)
    }

    return "redirect:/validation/v2/items/{itemId}"
  }

  private fun checkValidation(item: Item?, bindingResult: BindingResult) {
    item?.let {
      // 검증 로직
      if (!StringUtils.hasText(item.itemName)) {
        bindingResult.addError(FieldError("item", "itemName", "상품 이름은 필수입니다."))
      }
      // price 의 기본값으로 -1 을 세팅했기 때문에 null 체크는 무시
      if (item.price < 1000 || item.price > 1000000) {
        bindingResult.addError(FieldError("item", "price", "가격은 1,000 ~ 1,000,000 까지 허용합니다."))
      }

      if (item.quantity >= 9999) {
        bindingResult.addError(FieldError("item", "quantity", "수량은 최대 9,999 까지 허용합니다."))
      }

      // 복합 룰 검증
      if (item.quantity * item.price < 10000) {
        bindingResult.addError(ObjectError("item", "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = ${item.quantity * item.price}"))
      }
    }
  }

  private fun checkValidation2(item: Item?, bindingResult: BindingResult) {
    item?.let {
      // 검증 로직
      if (!StringUtils.hasText(item.itemName)) {
        bindingResult.addError(FieldError("item", "itemName", item.itemName, false, null, null, "상품 이름은 필수입니다."))
      }
      // price 의 기본값으로 -1 을 세팅했기 때문에 null 체크는 무시
      if (item.price < 1000 || item.price > 1000000) {
        bindingResult.addError(FieldError("item", "price", item.price, false, null, null, "가격은 1,000 ~ 1,000,000 까지 허용합니다."))
      }

      if (item.quantity >= 9999) {
        bindingResult.addError(FieldError("item", "quantity", item.quantity, false, null, null, "수량은 최대 9,999 까지 허용합니다."))
      }

      // 복합 룰 검증
      if (item.quantity * item.price < 10000) {
        bindingResult.addError(ObjectError("item", null, null, "가격 * 수량의 합은 10,000원 이상이어야 합니다. 현재 값 = ${item.quantity * item.price}"))
      }
    }
  }

  private fun checkValidation3(item: Item?, bindingResult: BindingResult) {
    item?.let {
      // 검증 로직
      if (!StringUtils.hasText(item.itemName)) {
        bindingResult.addError(FieldError("item", "itemName", item.itemName, false, arrayOf("required.item.itemName"), null, null))
      }
      // price 의 기본값으로 -1 을 세팅했기 때문에 null 체크는 무시
      if (item.price < 1000 || item.price > 1000000) {
        bindingResult.addError(FieldError("item", "price", item.price, false, arrayOf("range.item.price"), arrayOf(1000, 1000000), null))
      }

      if (item.quantity >= 9999) {
        bindingResult.addError(FieldError("item", "quantity", item.quantity, false, arrayOf("max.item.quantity"), arrayOf(9999), null))
      }

      // 복합 룰 검증
      if (item.quantity * item.price < 10000) {
        bindingResult.addError(ObjectError("item", arrayOf("totalPriceMin"), arrayOf(10000, item.quantity * item.price), null))
      }
    }
  }

  private fun checkValidation4(item: Item?, bindingResult: BindingResult) {
    item?.let {
      // 검증 로직
      if (!StringUtils.hasText(item.itemName)) {
        bindingResult.rejectValue("itemName", "required")
      }

      // price 의 기본값으로 -1 을 세팅했기 때문에 null 체크는 무시
      if (item.price < 1000 || item.price > 1000000) {
        bindingResult.rejectValue("price", "range", arrayOf(1000, 1000000), null)
      }

      if (item.quantity >= 9999) {
        bindingResult.rejectValue("quantity", "max", arrayOf(9999), null)
      }

      // 복합 룰 검증
      if (item.quantity * item.price < 10000) {
        bindingResult.reject("totalPriceMin", arrayOf(10000, item.quantity * item.price), null)
      }
    }
  }

  @GetMapping("/{itemId}/edit")
  fun editForm(@PathVariable itemId: Long, model: Model): String {
    val item = itemRepository.findById(itemId)
    model.addAttribute("item", item)
    return "validation/v2/editForm"
  }

  @PostMapping("/{itemId}/edit")
  fun edit(@PathVariable itemId: Long, @ModelAttribute item: Item): String {
    itemRepository.update(itemId, item)
    return "redirect:/validation/v2/items/{itemId}"
  }
}