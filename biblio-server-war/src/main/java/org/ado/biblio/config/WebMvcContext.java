package org.ado.biblio.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author sMeet, 02.02.15
 */
@Configuration
@ComponentScan("org.ado.biblio")
@EnableWebMvc
public class WebMvcContext extends WebMvcConfigurerAdapter {

// ----- STATIC ACCESSOR ---------------------------------------------------------------------------------------- //

// ----- TO BE IMPLEMENTED BY SUBCLASSES ------------------------------------------------------------------------ //

// ----- OVERWRITTEN METHODS ------------------------------------------------------------------------------------ //

// ----- GETTERS / SETTER --------------------------------------------------------------------------------------- //

// ----- INTERNAL HELPER ---------------------------------------------------------------------------------------- //

// ----- HELPER CLASSES ----------------------------------------------------------------------------------------- //

// ----- DEPENDENCY INJECTION ----------------------------------------------------------------------------------- //

}