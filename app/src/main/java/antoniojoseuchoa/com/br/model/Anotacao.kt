package antoniojoseuchoa.com.br.model



data class Anotacao(
    var id: Long,
    var title: String,
    var description: String
) {
    constructor(): this(0, "", "")
}