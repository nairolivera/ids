package abm

import java.util.Date;

class Usuario {

	String nombre

	String apellido

	Integer DNI

	Date fechaAlta=new Date()

	Date fechaBaja=new Date()

	String email

	String toString()
	{ "$email" }


	String password
	//int idRol (FK de la tabla rol)

    static constraints = {
    	email(email:true)
    	password(blank:false, password:true)
    }
}