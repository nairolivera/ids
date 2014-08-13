import abm.Area
import abm.Estado
import abm.Tipo
import abm.Ubicacion
import com.testapp.Role
import com.testapp.User
import com.testapp.UserRole
class BootStrap {

    def init = { servletContext ->
		
    		//Instanciar los estados
			
			def evaluar = new Estado(nombre: "A Evaluar").save(flush:true)
			def reparar = new Estado(nombre: "A Reparar").save(flush:true)
			def uso = new Estado(nombre: "En uso").save(flush:true)
			def donacion = new Estado(nombre: "A donacion").save(flush:true)
			def descarte = new Estado(nombre: "A descarte").save(flush:true)
			def baja = new Estado(nombre: "Baja").save(flush:true)
    
			//Instanciar las areas
			
			def s = new Area(nombrearea: "Sistemas").save(flush:true)
			def q = new Area(nombrearea: "Quimica").save(flush:true)
			def e = new Area(nombrearea: "Electrica").save(flush:true)
			def m = new Area(nombrearea: "Mecanica").save(flush:true)
			
			//Instanciar las ubicaciones
			
			def d = new Ubicacion(nombreubica: "Deposito").save(flush:true)
			def l = new Ubicacion(nombreubica: "Laboratorio").save(flush:true)
			def a = new Ubicacion(nombreubica: "Aula").save(flush:true)
			
			//Instanciar los tipos
			
			def simple = new Tipo(nombretipo: "Simple").save(flush:true)
			def compuesto = new Tipo(nombretipo: "Compuesto").save(flush:true)
			//instancio roles
			def adminRole = new Role(authority: 'ROLE_ADMIN').save(flush: true)
			def userRole = new Role(authority: 'ROLE_USER').save(flush: true)
			//instancio usuarios
			def usuario1 = new User(username: 'rprada', password: 'pass')
			usuario1.save(flush: true)
			def usuario2 = new User(username: 'pepito', password: 'password')
			usuario2.save(flush: true)
			//asigno roles a los usuarios
			UserRole.create usuario1, adminRole, true
			UserRole.create usuario2, adminRole, true
			//verifica que haya n usuarios
			assert User.count() == 2
			//verifica que haya m roles
			assert Role.count() == 2
			//verifica que haya n usuarios con roles
			assert UserRole.count() == 2
		}
	
		def destroy = {
		}
	}
