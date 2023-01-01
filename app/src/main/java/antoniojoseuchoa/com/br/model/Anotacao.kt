package antoniojoseuchoa.com.br.model


data class Anotacao(
    var id: String,
    var title: String,
    var description: String
) {
    constructor(): this("", "", "")
}