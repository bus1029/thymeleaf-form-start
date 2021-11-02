package hello.thymeleafformstart.domain.item

data class Item(var itemName: String, var price: Int, var quantity: Int) {
  var id: Long = 0L

  var open: Boolean = false
  var regions: List<String> = mutableListOf()
  var itemType: ItemType = ItemType.ETC
  var deliveryCode: String = DeliveryCode("NORMAL", "일반 배송").code

  constructor() : this("", -1, -1)
}
