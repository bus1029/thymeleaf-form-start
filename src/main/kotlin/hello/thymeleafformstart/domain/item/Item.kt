package hello.thymeleafformstart.domain.item

data class Item(var itemName: String, var price: Int, var quantity: Int) {
  var id: Long = 0L
}
