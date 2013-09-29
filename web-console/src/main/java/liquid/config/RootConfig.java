package liquid.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * TODO: Comments.
 * User: tao
 * Date: 9/22/13
 * Time: 11:24 PM
 */
@Configuration
@ComponentScan({"liquid.aop", "liquid.service"})
@EnableAspectJAutoProxy
public class RootConfig {}
