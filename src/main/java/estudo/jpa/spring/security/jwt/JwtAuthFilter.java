package estudo.jpa.spring.security.jwt;

import estudo.jpa.spring.service.imp.UsuarioServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    public JwtAuthFilter(JwtService jwtService, UsuarioServiceImp usuarioServiceImp) {
        this.jwtService = jwtService;
        this.usuarioServiceImp = usuarioServiceImp;
    }

    private JwtService jwtService;
    private UsuarioServiceImp usuarioServiceImp;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization !=null && authorization.startsWith("Bearer")){
            String token = authorization.split(" ")[1];

            boolean isValid = jwtService.tokenValido(token);

            if (isValid){
                String loginUsuario = jwtService.obterLoginUsuario(token);
                UserDetails usuario = usuarioServiceImp.loadUserByUsername(loginUsuario);
                UsernamePasswordAuthenticationToken user = new
                        UsernamePasswordAuthenticationToken(usuario,
                        null,
                        usuario.getAuthorities());
                user.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(user);
            }
        }
        filterChain.doFilter(request, response);
    }
}
