# it-service-ucenter-auth
ITMooc 认证服务

## 认证配置加载顺序

1. 授权服务器初始化
AuthorizationServerConfig
2. 认证管理器初始化
WebSecurityConfig
3. 授权服务器端点配置，配置认证管理器，令牌存储，UserDetailService
configure(AuthorizationServerEndpointsConfigurer endpoints)
4. 授权服务器加载数据库配置和客户端认证配置
configure(ClientDetailsServiceConfigurer clients)
5. 申请令牌
UserDetailsServiceImpl
loadloadUserByUsername 通过client_id从oauth_client_details表中获取client的信息
clientDetailsService.loadClientByClientId(username)
6. 用户认证
UserDetailsServiceImpl
loadloadUserByUsername
验证数据库user表用户信息，并将用户信息返回
7. CustomUserAuthenticationConverter
convertUserAuthentication
所有授权信息都返回到资源服务器，封装到JWT令牌中