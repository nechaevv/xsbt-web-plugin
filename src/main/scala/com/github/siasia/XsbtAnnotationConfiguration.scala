package com.github.siasia

import java.io.File
import org.eclipse.jetty.annotations.ClassNameResolver
import org.eclipse.jetty.util.resource.Resource
import org.eclipse.jetty.webapp.WebAppContext
import org.eclipse.jetty.annotations.{AnnotationConfiguration,AnnotationParser}

class XsbtAnnotationConfiguration(classpath: Seq[File]) extends AnnotationConfiguration {
  override def parseWebInfClasses (context: WebAppContext, parser: AnnotationParser) {
    classpath.foreach(path=>{
      parser.clearHandlers
      parser.registerHandlers(_discoverableAnnotationHandlers)
      parser.registerHandler(_classInheritanceHandler)
      parser.registerHandlers(_containerInitializerAnnotationHandlers)
      parser.parse(Resource.newResource(path), new ClassNameResolver {
	override def isExcluded(name:String) = false
	override def shouldOverride(name:String) = !context.isParentLoaderPriority
      })
    })
  }
}
