package com.example.demo.utils;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.repo.ICustomerRepo;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTTokenUtil {
	
	//HMAC uses symmetric key , same key for signing and parsing
//	 private String secret = "8e03ae0d-d4c4-4138-8d31-ee1da131ae03";
	 // RSA asymmetric key, private key for signing and public key for parsing
	 private static PublicKey publicKey;
	 private static PrivateKey  privateKey;
	 
	 @Autowired
	 ICustomerRepo customerRepo;
	 
		@PostConstruct
		public static void genKeys() {
			Map<String, Object> rsaKeys = new HashMap<>();

			try {
				rsaKeys = SecurityKeysGenUtil.loadKeysFromFiles("publickeygen.pub","privatekeygen.key");
			} catch (Exception e) {
				e.printStackTrace();
			}
			publicKey = (PublicKey) rsaKeys.get("public");
			privateKey = (PrivateKey) rsaKeys.get("private");
		}

	    public String extractUsername(String token) {
	        return extractClaim(token, Claims::getSubject);
	    }

	    public Date extractExpiration(String token) {
	        return extractClaim(token, Claims::getExpiration);
	    }

	    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
	        final Claims claims = extractAllClaims(token);
	        return claimsResolver.apply(claims);
	    }
	    private Claims extractAllClaims(String token) {
	        return Jwts.parser()
	        					.setSigningKey(publicKey)
	        					.parseClaimsJws(token)
	        					.getBody();
	    }

	    private Boolean isTokenExpired(String token) {
	        return extractExpiration(token).before(new Date());
	    }

	    public String generateToken(UserDetails userDetails) {
	        Map<String, Object> claims = new HashMap<>();
	        claims.put("roles", userDetails.getAuthorities());
	        return createToken(claims, userDetails.getUsername());
	    }

	    private String createToken(Map<String, Object> claims, String username) {
	    return Jwts.builder()
	    					.setClaims(claims)
	    					.setIssuer("DEMO APP")
	    					.setSubject(username)
	    					.setIssuedAt(new Date(System.currentTimeMillis()))
	    					.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
	    					.signWith(SignatureAlgorithm.RS512, privateKey)
	    					.compact();
	    }

	    public Boolean validateToken(String token, UserDetails userDetails) {
	        final String username = extractUsername(token);
	        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	    }
	    
}
