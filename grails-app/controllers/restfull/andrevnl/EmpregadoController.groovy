package restfull.andrevnl

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class EmpregadoController {

    EmpregadoService empregadoService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond empregadoService.list(params), model:[empregadoCount: empregadoService.count()]
    }

    def show(Long id) {
        respond empregadoService.get(id)
    }

    def create() {
        respond new Empregado(params)
    }

    def save(Empregado empregado) {
        if (empregado == null) {
            notFound()
            return
        }

        try {
            empregadoService.save(empregado)
        } catch (ValidationException e) {
            respond empregado.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'empregado.label', default: 'Empregado'), empregado.id])
                redirect empregado
            }
            '*' { respond empregado, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond empregadoService.get(id)
    }

    def update(Empregado empregado) {
        if (empregado == null) {
            notFound()
            return
        }

        try {
            empregadoService.save(empregado)
        } catch (ValidationException e) {
            respond empregado.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'empregado.label', default: 'Empregado'), empregado.id])
                redirect empregado
            }
            '*'{ respond empregado, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        empregadoService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'empregado.label', default: 'Empregado'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'empregado.label', default: 'Empregado'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
