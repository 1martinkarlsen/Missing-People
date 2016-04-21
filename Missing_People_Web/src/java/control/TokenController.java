package control;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import entity.Role;
import exception.UnknownServerException;
import java.util.Date;
import java.util.List;
import security.Secrets;

public class TokenController {
    
    public String createToken(String email, List<Role> roles) throws JOSEException, UnknownServerException {
        StringBuilder strBuilder = new StringBuilder();
        for (Role role : roles) {
            strBuilder.append(role.getRoleName());
            strBuilder.append(",");
        }
        
        String rolesAsString = strBuilder.substring(0, strBuilder.length() - 1);
        
        if(strBuilder.length() <= 0) 
            throw new UnknownServerException("Token error");
        
        String issuer = "usertoken-spost";
        JWSSigner signer = new MACSigner(Secrets.tokenSecret.getBytes());
        
        JWTClaimsSet claim = new JWTClaimsSet();
        claim.setSubject(email);
        claim.setClaim("email", email);
        claim.setCustomClaim("roles", rolesAsString);
        Date date = new Date();
        claim.setIssueTime(date);
        claim.setExpirationTime(new Date(date.getTime() + 1000*60*60));
        claim.setIssuer(issuer);
        
        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claim);
        signedJWT.sign(signer);
        
        return signedJWT.serialize(); 
    }
}
