package com.dailycodebuffer.jwt.controller;

import com.dailycodebuffer.jwt.entity.Token;
import com.dailycodebuffer.jwt.model.JwtRequest;
import com.dailycodebuffer.jwt.model.JwtResponse;
import com.dailycodebuffer.jwt.service.UserService;
import com.dailycodebuffer.jwt.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
@RestController
public class HomeController {

    @Autowired
    private JWTUtility jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @GetMapping("/getToken")
    public String fetchToken(@RequestParam(value = "email")String email) {
        if(userService.getToken(email) == null){
            UserDetails userDetails = userService.loadUserByUsername(email);
            String token = jwtUtility.generateToken(userDetails);
            Date timestamp = jwtUtility.getExpirationDateFromToken(token);
            Token curToken = new Token(email,token,timestamp);
            userService.saveToken(curToken);
            return token.toString();
        }else{
            String token = userService.getToken(email).getToken();
            Timestamp ts=new Timestamp(System.currentTimeMillis());
            Date CurDate=ts;
            Date future2minDate=ts;
            future2minDate.setTime(future2minDate.getTime()+ 120*1000);//Expire in 2 minutes
            Date ExpireDate = userService.getToken(email).getExpiration_timestamp();

            if(ExpireDate.before(future2minDate) || ExpireDate.before(CurDate)){
                userService.remove(token);
                UserDetails userDetails = userService.loadUserByUsername(email);
                token = jwtUtility.generateToken(userDetails);
                Date timestamp = jwtUtility.getExpirationDateFromToken(token);
                Token curToken = new Token(email,token,timestamp);
                userService.saveToken(curToken);
                return token.toString();
            }
            return token.toString();
        }
    }

    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest) throws Exception{

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
                            jwtRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }

        UserDetails userDetails = userService.loadUserByUsername(jwtRequest.getUsername());
        String token = jwtUtility.generateToken(userDetails);
        Date timestamp = jwtUtility.getExpirationDateFromToken(token);
        String email = jwtRequest.getUsername();
        Token curToken = new Token(email,token,timestamp);
        userService.saveToken(curToken);

        return  new JwtResponse(token);
    }
}
