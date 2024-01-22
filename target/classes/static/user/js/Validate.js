 var emailError = document.getElementById("email_error");
 var passwordError = document.getElementById("password_error");
      function Email() {
        var email = document.getElementById("email_input").value;
        if (email.length == 0) {
          emailError.innerHTML = "Email is required";
          return false;
        }
        if (!email.match(/^\w+[@]\w+[.]\w{3}$/)) {
          emailError.innerHTML = "Email invalid(Ex: thuong@gmail.com)";
          return false;
        }
        emailError.innerHTML = '';
        return true;
      }
      function Password() {
        var password = document.getElementById("password_input").value;
        if (password.length == 0) {
          passwordError.innerHTML = "Password is required";
          return false;
        }
        if (password.length < 8 || password.length > 12) {
          passwordError.innerHTML = "Must have 8-12 characters";
          return false;
        }
        passwordError.innerHTML = '';
        return true;
      }
      function SignIn() {
        if (!Email() || !Password()) {
          return false;
        }
      }