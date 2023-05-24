package com.nanoka.examen.security;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class JwtGenerador {
    //Método para crear un token por medio de la autenticación
    public String generateToken(Authentication authentication){
        String username = authentication.getName();
        Date tiempoActual = new Date();
        Date expiracionToken = new Date(tiempoActual.getTime() + ConstantesSeguridad.JWT_EXPIRATION_TOKEN);

        //Linea para generar el token
        String token = Jwts.builder() //Construimos un token JWT llamado token
                .setSubject(username) //Aca establecemos el nombre de usuario que está iniciando sesión
                .setIssuedAt(new Date()) //Establecemos la fecha de emisión del token en el momento actual
                .setExpiration(expiracionToken) //Establecemos la fecha de caducidad del token
                .signWith(SignatureAlgorithm.HS512,ConstantesSeguridad.JWT_FIRMA) /*Utilizamos este método para firmar
                nuestro token y de esta manera evitar la manipulación o modificación de este*/
                .compact(); //Este método finaliza la construcción del token y lo convierte en una cadena compacta
        return token;
    }

    //Método para extraer un username a partir de un token
    public String obtenerUsernameJwt(String token){
        Claims claims = Jwts.parser()
                            .setSigningKey(ConstantesSeguridad.JWT_FIRMA)
                            .parseClaimsJws(token)
                            .getBody();
        return claims.getSubject();
    }

    //Método para validar el token
    public Boolean validarToken(String token) {
        try {
            Jwts.parser().setSigningKey(ConstantesSeguridad.JWT_FIRMA).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Jwt ha expirado o está incorrecto");
        }
    }
}
