<%@ page import="abm.Persona" %>
<!DOCTYPE html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'persona.label', default: 'Persona')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
		<style>
			.panel-footer a:hover .md{
				color:black;
				text-decoration:none;
			}
			.panel-footer button:hover .md{
				color:black;
				text-decoration:none;
			}
		</style>
	</head>
	<body>
		<nav class="navbar navbar-default barra_acciones">
			<div class="container-fluid">
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-2">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="/${grailsApplication.config.nombreAplicacion}/persona/index"><i class="md md-arrow-back"></i></a>
					<a class="navbar-brand">Detalle</a>
				</div>
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-2">
					<!-- <form class="navbar-form navbar-left" action="busqueda" method="GET" role="search">
						<div class="form-group">
							<input type="text" class="form-control" name="query" placeholder="Buscar por denominacion...">
						</div>
						<button type="submit" class="btn btn-default"><i class="md md-search"></i></button>
					</form> -->
					<ul class="nav navbar-nav navbar-right">
						<li>
							<a data-toggle="collapse" href="#crearPersona" aria-expanded="false" aria-controls="crearPersona">
								<i class="md md-add"></i>
							</a>
						</li>
					</ul>
				</div>
			</div>
		</nav>
		<div class="collapse" id="crearPersona">
			<g:include controller="persona" action="create" />
		</div>
		<div class="container-fluid">
			<g:if test='${flash.message}'>
				<div class="alert alert-dismissible alert-success" role="alert">
					<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
					<i class="md md-error"></i> ${flash.message}
				</div>
			</g:if>
			<div class="panel panel-default">
				<!-- <div class="panel-heading">
					<g:message code="default.show.label" args="[entityName]"/>
				</div> -->
				<div class="panel-body">
					<ul class="list-group">
						<g:if test="${personaInstance?.nombre}">
							<li class="list-group-item">
								<strong>
									<span id="nombre-label" class="property-label">
										<g:message code="persona.nombre.label" default="Nombre" />
									</span>
								</strong>
								<span class="property-value" aria-labelledby="nombre-label">
									<g:fieldValue bean="${personaInstance}" field="nombre"/>
								</span>
							</li>
						</g:if>	
						<g:if test="${personaInstance?.apellido}">
							<li class="list-group-item">
								<strong>
									<span id="apellido-label" class="property-label">
										<g:message code="persona.apellido.label" default="Apellido" />
									</span>
								</strong>
								<span class="property-value" aria-labelledby="apellido-label">
									<g:fieldValue bean="${personaInstance}" field="apellido"/>
								</span>	
							</li>
						</g:if>
						<g:if test="${personaInstance?.dni}">
							<li class="list-group-item">
								<strong>
									<span id="dni-label" class="property-label">
										<g:message code="persona.dni.label" default="DNI" />
									</span>
								</strong>
								<span class="property-value" aria-labelledby="dni-label">
									<g:fieldValue bean="${personaInstance}" field="dni"/>
								</span>
							</li>
						</g:if>
						<g:if test="${personaInstance?.area}">
							<li class="fieldcontain list-group-item list-group-item">
								<strong>
									<span id="area-label" class="property-label"><g:message code="persona.area.label" default="Area" /></span>
								</strong>
								<span class="property-value" aria-labelledby="area-label">
									<g:link controller="area" action="show" id="${personaInstance?.area?.id}">${personaInstance?.area?.encodeAsHTML()}</g:link>
								</span>
							</li>
						</g:if>
						<g:if test="${personaInstance?.fechaAlta}">
							<li class="list-group-item">
								<strong>
									<span id="fechaAlta-label" class="property-label">
										<g:message code="persona.fechaAlta.label" default="Fecha Alta" />
									</span>
								</strong>
								<span class="property-value" aria-labelledby="fechaAlta-label">
									<g:formatDate format="dd-MM-yyyy" date="${personaInstance?.fechaAlta}" />
								</span>
							</li>
						</g:if>
						<g:if test="${personaInstance?.fechaBaja}">
							<li class="list-group-item">
								<strong>
									<span id="fechaBaja-label" class="property-label">
										<g:message code="persona.fechaBaja.label" default="Fecha Baja" />
									</span>
								</strong>
								<span class="property-value" aria-labelledby="fechaBaja-label">
									<g:formatDate format="dd-MM-yyyy" date="${personaInstance?.fechaBaja}" />
								</span>
							</li>
						</g:if>
					</ul>
					<g:form url="[resource:personaInstance, action:'delete']" method="DELETE" id="form_show_persona">
					</g:form>
				</div>
				<div class="panel-footer" >
					<g:link action="edit" resource="${personaInstance}" style="padding: 0px 12px; color:#777777"><i class="md md-edit"></i></g:link>
					<button class="btn btn-link" type="submit" form="form_show_persona" style="padding: 0px 12px; color:#777777">
						<i class="md md-delete"></i>
					</button>
				</div>
			</div>
		</div>
	</body>
</html>
