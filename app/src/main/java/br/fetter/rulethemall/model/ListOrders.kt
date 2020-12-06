package br.fetter.rulethemall.model

class ListOrders(list: List<Order>) {

    private val list: List<Order>

    init {
        this.list = list
    }

    fun getList(): List<Order> {
        return list
    }
}