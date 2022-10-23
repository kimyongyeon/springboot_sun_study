# 교재 삽질로그

# 시큐리티에 핵심개념은?

인증, 인가

# 사용자 금고 내용을 열어본다고 가정

1. 사용자는 은행에 가서 자신이 어떤 사람인지 자신의 신분증으로 자신을 **증명(인증)**
2. 은행에서는 사용자의 신분을 확인한다.
3. 은행에서 사용자가 금고를 열어 볼 수 있는 사람인지를 판단(인가⇒허가)
4. 만일 적절한 권리나 권한이 있는 사용자의 경우 금고를 열어준다. 

# 필터와 필터체이닝

**스프링 시큐리티에서 필터는 서블릿이나 JSP에서 사용하는 필터와 같은 개념이다.** 다만, 스프링 시큐리티에서는 스프링의 빈과 연동할수 있는 구조로 설계되어 있다.

일반적인 필터는 스프링의 빈을 사용할 수 없기 때문에 별도의 클래스를 상속받는 형태가 많다.

**스프링 시큐리티의 내부에는 여러개의 필터가 filter chain이라는 구조로 request를 처리한다.**
15개 정도의 필터가 동작하는 것을 확인 할 수 있다. 

개발시 필터를 확장하고 설정하면 스프링 시큐리티를 이용해서 다양한 형태의 로그인 처리가 가능하다.

- DB, 인메모리, jwt, ldap, oauth…

# 인증을 위한 AuthenticationManager

- AuthenticationManager : 토큰을 만든다.

```java
// 인증 
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // 사용자 계정은 user1
    auth.inMemoryAuthentication().withUser("user1")
            // 1111 패스워드 인코딩 결과 '{noloop}password'
            .password("$2a$10$ionYDEFQ9CiMjfvxc26CH.Mse2SzyuC658QUCCP2TCe.wympR0jWS")
            .roles("USER");
}

// 인가
@Override
protected void configure(HttpSecurity http) throws Exception {
    // https://github.com/HomoEfficio/dev-tips/blob/master/Spring%20Security%EC%99%80%20h2-console%20%ED%95%A8%EA%BB%98%20%EC%93%B0%EA%B8%B0.md
    http.authorizeRequests()
            .antMatchers("/sample/all").permitAll()
            .antMatchers("/sample/member").hasRole("USER")
            // h2-console + security
            .antMatchers(
                    "/h2-console/**"    // 여기!
            ).permitAll()
            .anyRequest().authenticated()
            .and()
            .headers().addHeaderWriter
                    (new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            .and()
            .csrf().ignoringAntMatchers("/h2-console/**")
    ;

    http.formLogin(); // 권한이 없는 경우 로그인 페이지로 이동 시킴
    http.csrf().disable(); // get 방식의 logout도 허용
    http.logout(); // 로그아웃 설정
}
```

- AuthenticationProvider : DB, 메모리상에 정보를 활용할지 대한 다양한 처리를 담당하는 객체

![Untitled](%E1%84%80%E1%85%AD%E1%84%8C%E1%85%A2%20%E1%84%89%E1%85%A1%E1%86%B8%E1%84%8C%E1%85%B5%E1%86%AF%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%20840adec76a874f9f9baa3bba3fc2ef96/Untitled.png)

# UserDetailsService

역할 : 실제로 인증을 위한 데이터를 가져오는 역할 

JPA로 Repository를 제작했다면 UserDetailsService를 활용해서 사용자의 인증 정보를 처리한다.

# filter 프로세스 플로우 이해하기

1. Authentication Manager ⇒ 
2. Authentication Provider(Authentication Manager가 어떻게 동작해야 하는지 결정한다.) ⇒ 
3. UserDetailService(실제인증처리) 

# antMatchers()는 **/*와 같은 앤트 스타일의 패턴

원하는 자원을 선택할 수 있다. swagger, h2-console, resource 제외 할때 많이 사용

예시) [https://hermeslog.tistory.com/587](https://hermeslog.tistory.com/587)

```java
@Override
    public void configure(HttpSecurity http) throws Exception {
	    http.authorizeRequests()
	            .antMatchers("/login**", "/web-resources/**", "/actuator/**").permitAll()
	            .antMatchers("/admin/**").hasAnyRole("ADMIN")
	            .antMatchers("/order/**").hasAnyRole("USER")
	            .anyRequest().authenticated();
}
```

# PasswordEncoder

springboot 2.0 부터는 인증을 위해서 반드시 PasswordEncoder를 지정해야만 한다.

`BCryptPasswordEncoder` : bcrypt라는 해시 함수를 이용해서 패스워드를 암호화하는 목적으로 설계된 클래스이다.

단방향 : 원래대로 복호화는 불가능하다. 매번 암호화된 값도 다르다.(길이는 동일)

```java
java.lang.IllegalArgumentException: There is no PasswordEncoder mapped for the id "null"
	at org.springframework.security.crypto.password.DelegatingPasswordEncoder$UnmappedIdPasswordEncoder.matches(DelegatingPasswordEncoder.java:289) ~[spring-security-crypto-5.7.3.jar:5.7.3]
	at org.springframework.security.crypto.password.DelegatingPasswordEncoder.matches(DelegatingPasswordEncoder.java:237) ~[spring-security-crypto-5.7.3.jar:5.7.3]
```

# ROLE_USER 라는 권한

설정에서 /sample/member는 USER라는 권한이 있도록 지정한 부분이 있습니다. 

이때 USER라는 단어는 ROLE_USER라는 상수와 같은 의미 입니다. 

`antMatchers("/sample/member").hasRole("USER")`

```java
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'springSecurityFilterChain' defined in class path resource [org/springframework/security/config/annotation/web/configuration/WebSecurityConfiguration.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [javax.servlet.Filter]: Factory method 'springSecurityFilterChain' threw exception; nested exception is java.lang.IllegalArgumentException: role should not start with 'ROLE_' since it is automatically inserted. Got 'ROLE_USER'
```

`ExpressionUrlAuthorizationConfigurer` class에서 ROLE_ 문자열을 붙여주고 있음.

```java
public ExpressionUrlAuthorizationConfigurer(ApplicationContext context) {
		String[] grantedAuthorityDefaultsBeanNames = context.getBeanNamesForType(GrantedAuthorityDefaults.class);
		if (grantedAuthorityDefaultsBeanNames.length == 1) {
			GrantedAuthorityDefaults grantedAuthorityDefaults = context.getBean(grantedAuthorityDefaultsBeanNames[0],
					GrantedAuthorityDefaults.class);
			this.rolePrefix = grantedAuthorityDefaults.getRolePrefix();
		}
		else {
			this.rolePrefix = "ROLE_";
		}
		this.REGISTRY = new ExpressionInterceptUrlRegistry(context);
	}
```

# CSRF 설정

사이트간 요청 위조 방어하기 위해 임의의 값을 만들어서 GET방식을 제외한 모든 요청 방식(POST, PUT, DELETE) 등에 포함시켜야만 정상적인 동작이 가능하다. 

![Untitled](%E1%84%80%E1%85%AD%E1%84%8C%E1%85%A2%20%E1%84%89%E1%85%A1%E1%86%B8%E1%84%8C%E1%85%B5%E1%86%AF%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%20840adec76a874f9f9baa3bba3fc2ef96/Untitled%201.png)

![Untitled](%E1%84%80%E1%85%AD%E1%84%8C%E1%85%A2%20%E1%84%89%E1%85%A1%E1%86%B8%E1%84%8C%E1%85%B5%E1%86%AF%E1%84%85%E1%85%A9%E1%84%80%E1%85%B3%20840adec76a874f9f9baa3bba3fc2ef96/Untitled%202.png)

# 시큐리티 실습 시나리오

/sample/all, /sample/member, /sample/admin url별로 권한주기

# h2-console 함께쓰기

- ****인증 면제 처리****
- ****CSRF 면제 처리****
- ****X-Frame-Options 면제 처리****