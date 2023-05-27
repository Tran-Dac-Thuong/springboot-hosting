$(document).ready(function(){
            $('.plus_btn').click(function(){
                productId = $(this).attr("pid");
                qtyInput = $("#quantity" + productId);
                newQty = parseInt(qtyInput.val()) + 1;
                if(newQty < 10){
                    qtyInput.val(newQty);
                }
            });

            $('.minus_btn').click(function(){
                productId = $(this).attr("pid");
                qtyInput = $("#quantity" + productId);
                newQty = parseInt(qtyInput.val()) - 1;
                if(newQty > 0){
                   qtyInput.val(newQty);
                }
            });
});