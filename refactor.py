import os
import re

base_dir = r"c:\Users\zyroo\Downloads\Habit_Up_2.0.0-master\Habit_Up_2.0.0-master\src\main\java\org\example"
controllers_dir = os.path.join(base_dir, "controllers")
services_dir = os.path.join(base_dir, "services")

# 1. Update Services to use ResourceNotFoundException instead of RuntimeException
for file_name in os.listdir(services_dir):
    if file_name.endswith("Service.java"):
        file_path = os.path.join(services_dir, file_name)
        with open(file_path, "r", encoding="utf-8") as f:
            content = f.read()
        
        # Add import if ResourceNotFoundException isn't there and we replace at least one
        if "new RuntimeException" in content and "ResourceNotFoundException" not in content:
            # We don't want to replace all RuntimeExceptions willy-nilly, but replacing them with ResourceNotFoundException 
            # or UnauthorizedException is what we want.
            # Most are "no encontrado" or "no existe".
            content = re.sub(r'new RuntimeException\(([^)]*(encontrado|existe)[^)]*)\)', r'new org.example.exceptions.ResourceNotFoundException(\1)', content)
            
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)

# 2. Update Controllers to use CustomUserDetails instead of @PathVariable Long userId
for file_name in os.listdir(controllers_dir):
    if file_name.endswith("Controller.java"):
        file_path = os.path.join(controllers_dir, file_name)
        with open(file_path, "r", encoding="utf-8") as f:
            content = f.read()

        # Regex to find @PathVariable Long userId
        # Also handle removing it from mapping paths e.g., @GetMapping("/user/{userId}") -> @GetMapping
        content = re.sub(r'/user/\{userId\}', '', content)
        content = re.sub(r'/\{userId\}', '', content) # General cases like /dashboard/{userId}
        
        # Replace method signatures @PathVariable Long userId -> @AuthenticationPrincipal org.example.security.CustomUserDetails userDetails
        content = re.sub(r'@PathVariable\s+Long\s+userId', r'@org.springframework.security.core.annotation.AuthenticationPrincipal org.example.security.CustomUserDetails userDetails', content)
        
        # Replace local usages of userId with userDetails.getId()
        # This is a bit tricky but usually it's just passing userId to methods.
        content = re.sub(r'\buserId\b', 'userDetails.getId()', content)
        
        with open(file_path, "w", encoding="utf-8") as f:
            f.write(content)

print("Refactor completed")
