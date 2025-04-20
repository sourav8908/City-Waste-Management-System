<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>404 - Page Not Found</title>
    <link rel="stylesheet" href="../css/style.css">
    <style>
        .error-container {
            text-align: center;
            padding: 50px 0;
        }
        .error-code {
            font-size: 72px;
            color: #e74c3c;
            margin-bottom: 20px;
        }
        .error-message {
            font-size: 24px;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="card">
            <div class="error-container">
                <div class="error-code">404</div>
                <div class="error-message">Page Not Found</div>
                <p>The page you are looking for does not exist or has been moved.</p>
                <div class="button-group">
                    <a href="<%=request.getContextPath()%>" class="btn primary-btn">Go to Home</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html> 