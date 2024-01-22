 var messageError = document.getElementById("message_error");
 var nameError = document.getElementById("name_error");
 var emailError = document.getElementById("email_error");
 var subjectError = document.getElementById("subject_error");
      function Message() {
            var message = document.getElementById("message").value;
            if (message.length == 0) {
              messageError.innerHTML = "Message is required";
              return false;
            }

            messageError.innerHTML = '';
            return true;
      }
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
      function Name() {
        var name = document.getElementById("name").value;
        if (name.length == 0) {
          nameError.innerHTML = "Name is required";
          return false;
        }

        nameError.innerHTML = '';
        return true;
      }
       function Subject() {
              var subject = document.getElementById("subject").value;
              if (subject.length == 0) {
                subjectError.innerHTML = "Subject is required";
                return false;
              }

              subjectError.innerHTML = '';
              return true;
            }
      function Send() {
        if (!Message() || !Email() || !Name || !Subject) {
          return false;
        }
      }