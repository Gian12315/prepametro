document.getElementById('search').addEventListener("click", function(e) {
    e.preventDefault();
    var searchTerm = document.getElementById('input').value;
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.open('GET', '/certificates/certificates.txt', true);
    xmlhttp.onreadystatechange = () => {
        if (xmlhttp.readyState == XMLHttpRequest.DONE && xmlhttp.status == 200) {
            var result = xmlhttp.responseText;
            var found = 0;
            var keypairs = result.split('\n');
            for (const keypair of keypairs) {

                var info = keypair.split('=');
                var currentId = info[0];
                var currentLink = info[1];
                if (currentId == searchTerm) {
                    found = 1;
                    window.open(currentLink);
                    break;
                }
            }

            if (found != 1) {
                alert('No se encontro el certificado.');
            }
        }

    }
    xmlhttp.send();
});
