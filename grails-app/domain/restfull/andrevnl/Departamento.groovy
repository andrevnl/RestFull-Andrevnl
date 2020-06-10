package restfull.andrevnl

class Departamento {

    String nome

    static hasMany = [empregados: Empregado]

    static constraints = {
    }
}
