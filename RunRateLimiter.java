import entities.User;
import enums.Tier;
import policyResolver.DefaultPolicyResolver;
import policyResolver.PolicyResolver;

import java.util.Scanner;

public class RunRateLimiter {
    public static void main(String[] args) {
        User alice = new User("1", Tier.FREE);
        User bob = new User("2", Tier.PREMIUM);
        User charlie = new User("3", Tier.ENTERPRISE);

        PolicyResolver resolver = new DefaultPolicyResolver();

        RateLimiter rateLimiter = new RateLimiter(resolver);

        Scanner s = new Scanner(System.in);
        while(true){
            String input = s.nextLine();
            switch(input) {
                case "A":
                    System.out.println("Request for Alice: " + buildMessage(alice, rateLimiter));
                    continue;
                case "B":
                    System.out.println("Request for Bob: " + buildMessage(bob, rateLimiter));
                    continue;
                case "C":
                    System.out.println("Request for Charlie: " + buildMessage(charlie, rateLimiter));
                    continue;
                default:
                    System.out.println("Invalid input. Exiting...");
                    break;
            }
        }

    }

    private static String buildMessage(User user, RateLimiter rateLimiter){
        boolean allowed = rateLimiter.isRequestAllowed(user);
        return allowed ? "Request allowed " : "OHHHHH  NOOOO!!! \n Request rate limited" ;
    }

}
