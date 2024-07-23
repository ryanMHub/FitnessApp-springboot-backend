package com.ryanmhub.fitnessapp.config.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.internal.Function;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expirationMs}")
    private long jwtExpirationMs;

    @Value("${jwt.refresh-expiration}")
    private long jwtRefreshExpiration;

    //Todo: Double check appears I have this code twice Alternative is getSignInKey()
    private SecretKey getSecretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(jwtSecret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    public String generateJwtToken(UserDetails userDetails){
        return generateJwtToken(new HashMap<>(), userDetails);
    }

    public String generateJwtToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtExpirationMs);
    }

    public String generateRefreshToken(UserDetails userDetails){
        return generateRefreshToken(new HashMap<>(), userDetails);
    }

    public String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return buildToken(claims, userDetails, jwtRefreshExpiration);
    }

    //Todo: When and where should I insert Bearer
    private String buildToken(Map<String, Object> claims, UserDetails userDetails, long jwtExpirationMs) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    //Return the roles in a List from claims
    public List<String> extractRoles(String token){
        return extractClaim(token, this::getRoles);
    }

    //Converts 'roles' claim from json to List<String> using Jackson
    //Todo: handle exception. Go through all methods to determine if any throw an exception that needs to be resolved
    //Todo: There is probably a better way to do this
    public List<String> getRoles(Claims claims){
        try{
            ObjectMapper mapper = new ObjectMapper();
            //System.out.println(claims.get("roles"));
            return mapper.readValue(claims.get("roles").toString(), new TypeReference<List<String>>() {});
        } catch(Exception ex){
            System.out.println(ex);
            return new ArrayList<String>();
        }
    }

    //Todo: handle exception
    //Todo: make this function generic, How to deal with exception
    //Input object to serialize, return serialized string of object
    public String serializeObject(List<String> item) {
        ObjectMapper mapper = new ObjectMapper();
        try{
            return mapper.writeValueAsString(item);
        } catch(JsonProcessingException ex){
            return "[\"error\"]"; //Todo: maybe I should return something else or pass an error message
        }
    }

    //The JWT token is validated and claims is returned. Expiration is checked. Then username and authorities are extracted generated and
    //a basic User class is returned to caller to use in SecurityContext or other need use case.
    public User loadUserWithToken(String jwtToken){
        try{
            Claims claims = extractAllClaims(jwtToken);
            //Todo: check if token is revoked
            if(isTokenExpired(claims)) {
                //Todo: Expired token response
                //System.out.println("Expired Token");
                return null;
            }
            String username = claims.getSubject();
            //System.out.println("Username = " + username);
            List<String> roles = getRoles(claims);
            //roles.forEach(System.out::println);
            List<GrantedAuthority> authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            //System.out.println(authorities);
            return new User(username, "", authorities);
        } catch (Exception ex){
            //Todo: Is a return message needed?
            System.out.println("JWT not Valid From claims extraction");
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //Todo: handle error
    private Claims extractAllClaims(String token){
        return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }
}
