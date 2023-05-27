var emailError = document.getElementById("email_error");
      function Email() {
        var email = document.getElementById("email_input").value;
        if (email.length == 0) {
          emailError.innerHTML = "Email is required";
          return false;
        }
        if (!email.match(/^\w+[@]\w+[.]\w{3}$/)) {
          emailError.innerHTML = "@gmail.com";
          return false;
        }
        emailError.innerHTML = '';
        return true;
      }

      function Submit() {
        if (!Email()) {
          return false;
        }
      }