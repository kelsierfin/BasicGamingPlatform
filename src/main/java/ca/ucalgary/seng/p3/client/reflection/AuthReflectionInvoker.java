package ca.ucalgary.seng.p3.client.reflection;

import java.lang.reflect.Method;

/**
 * A "magic method finder" that uses reflection to call methods on the authentication service.
 * This class eliminates the need to create adapter methods for each authentication operation.
 */
public class AuthReflectionInvoker {
    private static AuthReflectionInvoker instance;
    private final Object authService;

    // Singleton pattern
    public static AuthReflectionInvoker getInstance() {
        if (instance == null) {
            // Create the authentication service instance - this could be any class that contains
            // the required authentication methods
            Object authServiceInstance = createAuthService();
            instance = new AuthReflectionInvoker(authServiceInstance);
        }
        return instance;
    }

    private AuthReflectionInvoker(Object authService) {
        this.authService = authService;
    }

    /**
     * The "magic" method that can call any method on the authentication service by name.
     *
     * @param methodName The name of the method to call
     * @param args The arguments to pass to the method
     * @return The result of the method call
     */
    public Object invoke(String methodName, Object... args) {
        try {
            // Get the parameter types from the arguments
            Class<?>[] paramTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                paramTypes[i] = args[i] != null ? args[i].getClass() : null;
            }

            // Find the method with exact parameter types first
            Method method = findMethodExact(methodName, paramTypes);

            // If not found, try to find a compatible method
            if (method == null) {
                method = findMethodCompatible(methodName, args, paramTypes);
            }

            if (method == null) {
                throw new NoSuchMethodException("No matching method found: " + methodName);
            }

            // Make it accessible (in case it's private or protected)
            method.setAccessible(true);

            // Invoke the method and return the result
            return method.invoke(authService, args);
        } catch (Exception e) {
            System.err.println("Error invoking method " + methodName + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Find a method with the exact parameter types.
     */
    private Method findMethodExact(String methodName, Class<?>[] paramTypes) {
        try {
            // Replace null parameter types with Object.class for the search
            Class<?>[] searchParamTypes = new Class<?>[paramTypes.length];
            for (int i = 0; i < paramTypes.length; i++) {
                searchParamTypes[i] = paramTypes[i] != null ? paramTypes[i] : Object.class;
            }
            return authService.getClass().getMethod(methodName, searchParamTypes);
        } catch (NoSuchMethodException e) {
            // Method not found with exact types
            return null;
        }
    }

    /**
     * Find a method with compatible parameter types.
     * This is more forgiving than the exact match and can handle subclasses.
     */
    private Method findMethodCompatible(String methodName, Object[] args, Class<?>[] paramTypes) {
        Method[] methods = authService.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().equals(methodName) && method.getParameterCount() == args.length) {
                Class<?>[] methodParamTypes = method.getParameterTypes();
                boolean match = true;

                for (int i = 0; i < methodParamTypes.length; i++) {
                    // Skip null parameters
                    if (args[i] == null) continue;

                    // Check if the argument is assignable to the parameter type
                    if (!methodParamTypes[i].isAssignableFrom(args[i].getClass())) {
                        match = false;
                        break;
                    }
                }

                if (match) {
                    return method;
                }
            }
        }

        return null;
    }

    /**
     * Create the authentication service instance.
     * This is a placeholder for your actual implementation.
     */
    private static Object createAuthService() {
        return new ca.ucalgary.seng.p3.network.NetworkAuthenticationService();  // ✅ Real login implementation
    }


    /**
     * Dummy authentication service for this example.
     * Replace this with your actual authentication implementation.
     */
    private static class AuthenticationService {
        public boolean login(String username, String password) {
            // Implementation would go here
            System.out.println("Login called with: " + username + ", " + password);
            return true;
        }

        public String guestLogin() {
            // Implementation would go here
            String guestName = "Guest" + (int)(Math.random() * 10000);
            System.out.println("Guest login generated: " + guestName);
            return guestName;
        }

        public boolean register(String username, String password, String email) {
            // Implementation would go here
            System.out.println("Register called with: " + username + ", " + password + ", " + email);
            return true;
        }

        public boolean logout(String username) {
            // Implementation would go here
            System.out.println("Logout called for: " + username);
            return true;
        }

        // Add more authentication methods as needed
    }
}