$(document).ready(function () {
    loadItems();
    addDollar();
    addQuarter();
    addDime();
    addNickel();
    makePurchase();
    changeReturn();
});

function loadItems() {
    var rowVal = 1;
    var itemNum = 1;

    $.ajax({
        type: 'GET',
        url: 'http://tsg-vending.herokuapp.com/items',
        success: function (items) {

            var length = items.length;
            var numRows = length / 3;
            if (length % 3 != 0) {
                numRows = Math.ceil(numRows);
            }

            $.each(items, function (index, item) {

                for (var i = 1; i <= numRows; i++) {
                    $('#content').append('<div class="row" id="row' + i + '"></div>');
                }

                var rowName = '#row' + rowVal;
                var contentRows = $(rowName);
                var id = item.id;
                var name = item.name;
                var price = item.price;
                var quantity = item.quantity;
                var row = '';

                row += '<div class="col-4 text-center"><div class="btn btn-lg border btnResize" onclick="updateMessages('
                    + id + ',' + itemNum + ',' + quantity + ',' + price + ')"><h5 class="display-5 text-left">'
                    + itemNum + '</h5>';
                row += '<p class="text-center">' + name + '</p>';
                row += '<p class="text-center">$' + price + '</p>';
                row += '<p class="text-center">Quantity: ' + quantity + '</p>' + '</div>' + '</div>';

                contentRows.append(row);

                itemNum++;
                if (itemNum > 3 && itemNum % 3 == 1) {
                    rowVal++;
                }
            })
        },
        error: function () {
            $('#errorMessages')
                .append($('<li>')
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text('Error calling web service. Please try again later.'));
        }
    })
}

function updateMessages(id, itemNum, quantity, price) {
    $('#itemId').val(id);
    $('#itemPrice').val(price);

    if (quantity == 0) {
        $('#message').val('Sold out');
        $('#purchaseButton').attr('disabled', true);
    } else {
        $('#message').val('Available');
        $('#purchaseButton').attr('disabled', false);
    }

    $('#itemNum').val(itemNum);
}

function makePurchase() {

    $('#purchaseButton').click(function (event) {

        var id = $('#itemId').val();
        var amount = $('#moneyInput').val();

        $.ajax({
            type: 'POST',
            url: 'http://tsg-vending.herokuapp.com/money/' + amount + '/item/' + id,
            success: function (changes) {
                var quarters = changes.quarters;
                var dimes = changes.dimes;
                var nickels = changes.nickels;
                var pennies = changes.pennies;

                var changeMsg = quarters + ' quarters, ' + dimes + ' dimes, '
                    + nickels + ' nickels, ' + pennies + ' pennies';

                $('#changes').val(changeMsg);
                $('#message').val('Thank You!');

            },

            error: function (xhr, status, error) {
                var err = eval("(" + xhr.responseText + ")");
                $('#message').val(err.message);
            }
        })

    });
}

function changeReturn() {

    $('#changeReturnButton').click(function () {
        $('#moneyInput').val('');
        $('#message').val('');
        $('#itemNum').val('');
        $('#changes').val('');
        $('#content').empty();
        loadItems();
    });
}

function addDollar() {
    $('#addDollar').click(function (event) {
        if ($('#moneyInput').val() === '') {
            $('#moneyInput').val(0);
        }
        var moneyInput = $('#moneyInput').val();
        doubleMoneyInput = parseFloat(moneyInput);

        doubleMoneyInput += 1.00;
        $('#moneyInput').val(doubleMoneyInput.toFixed(2));
    });
}

function addQuarter() {
    $('#addQuarter').click(function (event) {
        if ($('#moneyInput').val() === '') {
            $('#moneyInput').val(0);
        }
        var moneyInput = $('#moneyInput').val();
        doubleMoneyInput = parseFloat(moneyInput);

        doubleMoneyInput += 0.25;
        $('#moneyInput').val(doubleMoneyInput.toFixed(2));
    });
}

function addDime() {
    $('#addDime').click(function (event) {
        if ($('#moneyInput').val() === '') {
            $('#moneyInput').val(0);
        }
        var moneyInput = $('#moneyInput').val();
        doubleMoneyInput = parseFloat(moneyInput);

        doubleMoneyInput += 0.10;
        $('#moneyInput').val(doubleMoneyInput.toFixed(2));
    });
}

function addNickel() {
    $('#addNickel').click(function (event) {
        if ($('#moneyInput').val() === '') {
            $('#moneyInput').val(0);
        }
        var moneyInput = $('#moneyInput').val();
        doubleMoneyInput = parseFloat(moneyInput);

        doubleMoneyInput += 0.05;
        $('#moneyInput').val(doubleMoneyInput.toFixed(2));
    });
}

