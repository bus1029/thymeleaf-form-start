package hello.thymeleafformstart.domain.item

data class Item(var itemName: String, var price: Int, var quantity: Int) {
  var id: Long = 0L

  var open: Boolean = false
  var regions: List<String> = mutableListOf()
  var itemType: ItemType = ItemType.ETC
  var deliveryCode: DeliveryCode = DeliveryCode()

  constructor() : this("", -1, -1)
}
