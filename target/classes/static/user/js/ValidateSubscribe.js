
 var emailError = document.getElementById("email_error");

      function Email() {
              var email = document.getElementById("email").value;
              if (email.length == 0) {
                emailError.innerHTML = "Email is required";
                return false;
              }
              if (!email.match(/^\w+[@]\w+[.]\w{3}$/)) {
                emailError.innerHTML = "Email invalid(Ex: @gmail.com)";
                return false;
              }
              emailError.innerHTML = '';
              return true;
      }

      function Subscribe() {
        if (!Email()) {
          return false;
        }
      }