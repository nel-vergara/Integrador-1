package com.medina.fragrances.fragrance_app.security;

import com.medina.fragrances.fragrance_app.model.Usuario;
import com.medina.fragrances.fragrance_app.repository.UsuarioRepository;
import com.medina.fragrances.fragrance_app.service.AppUserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(AppUserService appUserService,
                                                            BCryptPasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                               DaoAuthenticationProvider authenticationProvider,
                                               AuthenticationSuccessHandler authenticationSuccessHandler,
                                               LogoutSuccessHandler logoutSuccessHandler) throws Exception {
    return http
            .authenticationProvider(authenticationProvider)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(
                            "/",
                            "/index.html",
                            "/admin/productos/exportar", // <-- AÑADIDO AQUÍ: Acceso libre para cualquier usuario
                            "/catalogo/**",
                            "/catalogo.html",
                            "/catalogo-dinamico",
                            "/catalogo-estatico",
                            "/marcas",
                            "/marcas.html",
                            "/decants",
                            "/decants.html",
                            "/contacto",
                            "/contacto.html",
                            "/contacto/enviar",
                            "/registro",
                            "/login",
                            "/auth/registrar",
                            "/api/auth/**",
                            "/privacidad",
                            "/privacidad.html",
                            "/terminos",
                            "/terminos.html",
                            "/css/**",
                            "/img/**",
                            "/main/**",
                            "/h2-console/**"
                    ).permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/productos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.PUT, "/api/productos/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/productos/**").hasRole("ADMIN")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/checkout/**").hasAnyRole("CLIENTE", "ADMIN")
                    .requestMatchers("/carrito", "/carrito.html").hasAnyRole("CLIENTE", "ADMIN")
                    .anyRequest().authenticated()
            )
            .formLogin(login -> login
                    .loginPage("/login")
                    .loginProcessingUrl("/auth/ingresar")
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(authenticationSuccessHandler)
                    .failureUrl("/login?error=true")
                    .permitAll()
            )
            .logout(logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/cerrar-sesion", "GET"))
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .permitAll()
            )
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .build();
}

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(UsuarioRepository usuarioRepository) {
        return (request, response, authentication) -> {
            Usuario usuario = usuarioRepository.findByEmail(authentication.getName()).orElse(null);
            if (usuario != null) {
                HttpSession session = request.getSession();
                session.setAttribute("usuarioId", usuario.getId());
                session.setAttribute("nombreUsuario", usuario.getNombre());
                session.setAttribute("rolUsuario", usuario.getRol());
                session.setAttribute("adminAutenticado", "ADMIN".equals(usuario.getRol()));

                if ("ADMIN".equals(usuario.getRol())) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                    return;
                }
            }

            String redirect = request.getParameter("redirect");
            if (redirect != null && redirect.startsWith("/") && !redirect.startsWith("//")) {
                response.sendRedirect(request.getContextPath() + redirect);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/");
        };
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> response.sendRedirect(request.getContextPath() + "/login?logout=true");
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
