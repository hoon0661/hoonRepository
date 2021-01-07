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
            $.each(items, function (index, item) {

                var rowName = '#row' + rowVal;
                var contentRows = $(rowName);

                var id = item.id;
                var name = item.name;
                var price = item.price;
                var quantity = item.quantity;
                var row = '';
                if (itemNum > 9 && itemNum % 3 == 1) {
                    row += '<div class="row" id="additionalRow"><div class="col-md-8"><div class="row" id="row' + rowVal + '">';
                }

                row += '<div class="col-md-4"><div class="btn btn-lg border" onclick="updateMessages(' + id + ',' + itemNum + ',' + quantity + ',' + price + ')"><h5 class="display-5 text-left" id="itemNum1">' + itemNum + '</h5>';
                row += '<p class="text-center">' + name + '</p>';
                row += '<p class="text-center">$' + price + '</p>';
                row += '<p class="text-center">Quantity: ' + quantity + '</p>' + '</div>' + '</div>';

                if (itemNum > 9 && itemNum % 3 == 1) {
                    row += '</div></div></div>';
                    $('#page').append(row);
                } else {
                    contentRows.append(row);
                }

                itemNum++;
                if (itemNum % 3 == 1) {
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
        alert(amount);
        alert(id);

        $.ajax({
            type: 'POST',
            url: 'http://tsg-vending.herokuapp.com/money/' + amount + '/item/' + id,
            success: function (changes) {
                var quarters = changes.quarters;
                var dimes = changes.dimes;
                var nickels = changes.nickels;
                var pennies = changes.pennies;

                var row = quarters + ' quarters, ' + dimes + ' dimes, '
                    + nickels + ' nickels, ' + pennies + ' pennies';

                $('#changes').val(row);
                $('#message').val('Thank You!');
            },
            error: function (error) {
                alert(Object.keys(error).length);
                var message = error.message;
                $('#message').val(message);
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
        $('#row1').empty();
        $('#row2').empty();
        $('#row3').empty();
        $('#additionalRow').remove();


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

