package stay.app.app.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class Jwt {

    @Value("${ACCESS_KEY}")
    String accessKey;

    private final Long expiredTime = 1000 * 60L * 60L;

    public String CreateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(username)
                .setHeader(createHeader())
                .setClaims(createClaims(username))
                .setExpiration(new Date(now.getTime() + expiredTime))
                .signWith(SignatureAlgorithm.HS256, accessKey)
                .compact();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return claims;
    }

    ///////////////// decoded////////////////////////

    private Claims getClaims(String token) {
        // return
        // Jwts.parser().setSigningKey(Base64.getEncoder().encodeToString(AccessKey.getBytes())).parseClaimsJws(token).getBody();
        return Jwts.parser().setSigningKey(accessKey).parseClaimsJws(token).getBody();
    }

    public String VerifyToken(String token) {
        return (String) getClaims(token).get("username");
    }


}
