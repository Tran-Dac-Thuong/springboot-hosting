      var addressError = document.getElementById("address-error");
      var numberError = document.getElementById("number-error");
      var firstError = document.getElementById("first-error");
      var lastError = document.getElementById("last-error");
      function Address() {
        var address = document.getElementById("address").value;
        if (address.length == 0) {
          addressError.innerHTML = "Address is required";
          return false;
        }

        addressError.innerHTML = '';
        return true;
      }
      function Number() {
        var number = document.getElementById("number").value;
        if (number.length == 0) {
          numberError.innerHTML = "Phone is required";
          return false;
        }
         if (isNaN(number)) {
             numberError.innerHTML = "Phone must be number";
             return false;
         }
        if (number.length < 10) {
          numberError.innerHTML = "Phone must have at least 10 digits";
          return false;
        }

        numberError.innerHTML = '';
        return true;
      }
       function First() {
             var first = document.getElementById("first").value;
             if (first.length == 0) {
                firstError.innerHTML = "First name is required";
                return false;
             }

             firstError.innerHTML = '';
             return true;
       }
       function Last() {
           var last = document.getElementById("last").value;
           if (last.length == 0) {
               lastError.innerHTML = "Last name is required";
               return false;
           }

           lastError.innerHTML = '';
           return true;
       }
      function Submit() {
        if (!First() || !Last() || !Phone() || !Address()) {
          return false;
        }
      }