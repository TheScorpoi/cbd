
digitNonRepited = function () {
    var counter = 0;
    cursor = db.phones.find();
    loop: while (cursor.hasNext()) {
        var phone = cursor.next();
        var num = phone.display.split('-')[1];
        for (var i = 0; i < num.length; i++) {
            for (var j = 0; j < i; j++) {
                if (num[i] == num[j]) {
                    continue loop;
                }
            }
        }
        counter++;
        print(num)
    }
    print("Total de numeros com digitos não repetidos: ", counter);
}