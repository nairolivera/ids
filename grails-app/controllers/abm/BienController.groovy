package abm


import com.testapp.UserRole
import com.testapp.User
import com.testapp.Role
import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.SpringSecurityService
import grails.gorm.*


@Secured(['ROLE_SUPERVISOR','ROLE_ENCARGADO','ROLE_OPERARIO'])
@Transactional(readOnly = true)
class BienController {
     def springSecurityService
    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def permiso(){
        return Role.findById( idRol() ).authority
    }
     def idRol(){
        def idU = User.findById( idUser() )
        return  UserRole.findByUser( idU ).role.id
    }
    def idUser(){
        return springSecurityService.loadCurrentUser().id
    }
    def idPersona(){
        return  PersonaUser.findByUserId( idUser() ).personaId
    }
    def mostrarBienesSegunPermiso(){
        //--si el rol es Admin, traigo todos los bienes.En caso contrario,
        //traigo los bienes que correspondan al area de la persona
        if (permiso() == 'ROLE_SUPERVISOR')
            return Bien.findAll()
        else{
            def areaUser = Persona.findById(idPersona()).area
            return Bien.findAllByArea(areaUser)
        }
    }
    def bienesSegunEstado(ArrayList listadoBienes, String nombreEstado){
        def estado = Estado.findByNombre(nombreEstado)
        return listadoBienes.findAll{it.estado.id == estado.id}
    }
    def contadorBienes(){
        def lista = (mostrarBienesSegunPermiso()).size()
        return lista
    }

    def bienesAEvaluar(){
        def listaAE = bienesSegunEstado(mostrarBienesSegunPermiso(), "A Evaluar")
        return listaAE
    }

    def bienesEnUso(){
        def listaEU = bienesSegunEstado(mostrarBienesSegunPermiso(), "En uso")
        return listaEU
    }

    def bienesAReparar(){
        def listaAR = bienesSegunEstado(mostrarBienesSegunPermiso(), "A Reparar")
        return listaAR
    }
    
    def bienesADonacion(){
        def listaAD = bienesSegunEstado(mostrarBienesSegunPermiso(), "A donacion")
        return listaAD
    }

    def bienesADescarte(){
        def listaADs = bienesSegunEstado(mostrarBienesSegunPermiso(), "A descarte")
        return listaADs
    }

    def bienesBaja(){
        def listaB = bienesSegunEstado(mostrarBienesSegunPermiso(), "Baja")
        return listaB
    }
    def index(Integer max) {
        //seteo el maximo a mostrar
        params.max = Math.min(max ?: 10, 100)
        respond mostrarBienesSegunPermiso(), model:[bienInstanceCount: Bien.count()] ,view:'index'
    }
    
    def busqueda(Integer max) {
        def query = params.query
        def bienList
        if (permiso() == 'ROLE_SUPERVISOR')
            bienList = Bien.findAll("from Bien where INSTR(nombreBien,?)>0",[query])
        else{
            def areaUser = Persona.findById(idPersona()).area
            bienList = Bien.findAll("from Bien where INSTR(nombreBien,?)>0 and area_id = ?",[query,areaUser])
        }
        params.max = Math.min(max ?: 10, 100)
        respond bienList, model:[ bienInstanceCount: Bien.count()],view:'index';
    }    
    
    def estadoAevaluar(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bienesAEvaluar(), model:[ bienInstanceCount: Bien.count()],view:'index';
    }
    def estadoAreparar(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bienesAReparar(),model:[ bienInstanceCount: Bien.count()],view:'index'
    }
    def estadoEnUso(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bienesEnUso(), model:[ bienInstanceCount: Bien.count()],view:'index'     
    }

    def estadoAdonacion(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bienesADonacion(), model:[ bienInstanceCount: Bien.count()],view:'index'     
    }
    def estadoAdescarte(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bienesADescarte() ,model:[ bienInstanceCount: Bien.count()],view:'index'     
     
    }
    def estadoBaja(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond bienesBaja(), model:[ bienInstanceCount: Bien.count()],view:'index'     
     
    }
    def enviarMail(String contenidoMail, String destinatario){ 
        sendMail {
           to destinatario
           cc "gise.cpna@gmail","ayestaranguillermo@gmail.com","romina.prada@gmail.com","nair.olivera.utn@gmail.com"
           from "patronus.ids@gmail.com"
           subject "Patronus"
           text contenidoMail   
        }
    }
    
    @Secured(['ROLE_SUPERVISOR','ROLE_ENCARGADO'])
    def grafico(){
            def a = bienesAEvaluar().size()
            def b = bienesEnUso().size()
            def c = bienesAReparar().size()
            def d = bienesADonacion().size()
            def e = bienesADescarte().size()
            def f = bienesBaja().size()  
        
        def opt =['#21AAFF', '#e6693e', '#ec8f6e', '#f3b49f', '#f6c7b6','#e6693e']
       
        render(view:'grafico' ,model:[a:a,b:b,c:c,d:d,e:e,f:f])
    }
    def noticias(){
        //Es provisorio, debería traerme los de ésta semana
        def cantAE = bienesAEvaluar().size()
        def cantEU = bienesEnUso().size()
        def cantAR = bienesAReparar().size()
        def cantAD = bienesADonacion().size()
        def cantADsc = bienesADescarte().size()
        def cantB = bienesBaja().size()
    render(
            view:'noticias',  
            model:
            [
                cantAE:cantAE,
                cantEU:cantEU,
                cantAR:cantAR,
                cantAD:cantAD,
                cantADsc:cantADsc,
                cantB:cantB
            ]
        )

    }

    def show(Bien bienInstance) {
        respond bienInstance
    }

    def create() {
        respond new Bien(params)
    }

    @Transactional
    def save(Bien bienInstance) {
        if (bienInstance == null) {
            notFound()
            return
        }

        if (bienInstance.hasErrors()) {
            respond bienInstance.errors, view:'create'
            return
        }

        bienInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'bien.label', default: 'Bien'), bienInstance.nombreBien])
                redirect bienInstance
            }
            '*' { respond bienInstance, [status: CREATED] }
        }
    }
    
    def edit(Bien bienInstance) {
        respond bienInstance
    }

    @Transactional
    def update(Bien bienInstance) {
        if (bienInstance == null) {
            notFound()
            return
        }

        if (bienInstance.hasErrors()) {
            respond bienInstance.errors, view:'edit'
            return
        }

        bienInstance.save flush:true
 
        def cuerpoMail= "Se ha modificado el bien :"+bienInstance.descripcion+". \nSu estado cambi&oacute a &quote"+bienInstance.estado.nombre+"&quote"
            println(cuerpoMail)
       // enviarMail(cuerpoMail,"pmdisanti@gmail.com")

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Bien.label', default: 'Bien'), bienInstance.nombreBien])
                redirect bienInstance
            }
            '*'{ respond bienInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Bien bienInstance) {

        if (bienInstance == null) {
            notFound()
            return
        }

        bienInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Bien.label', default: 'Bien'), bienInstance.nombreBien])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'bien.label', default: 'Bien'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
