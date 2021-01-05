$(document).ready(function () {
    initialPage();
    loadWeather();
});

function loadWeather() {
    $('#button').click(function (event) {
        var zip = $('#zipcode').val();
        var unit = $('#unit').val();
        setUnit();
        $('#errorMessages').empty();
        $('#currentWeather').hide();
        $('#fiveDays').hide();
        $('#currentWeather').show('slow');
        $('#fiveDays').show('slow');
        var weatherDetail = $('#weatherDetail');
        $('#weatherDescription').empty();
        for (var i = 1; i <= 5; i++) {
            var strDay = '#day' + i;
            $(strDay).empty();
        }

        $.ajax({
            type: 'GET',
            url: 'http://api.openweathermap.org/data/2.5/weather?zip=' + zip + ',us&units=' + unit + '&appid=c5bf70503882c71db48436245d185d2c',
            success: function (weather) {
                var temperature = weather.main.temp;
                var humidity = weather.main.humidity;
                var wind = weather.wind.speed;
                var imageIcon = weather.weather[0].icon;
                var cityName = weather.name;
                var main = weather.weather[0].main;
                var description = weather.weather[0].description;

                var imgUrl = 'http://openweathermap.org/img/w/' + imageIcon + '.png';

                $('#weatherImage').attr('src', imgUrl).text(main + ': ' + description);
                $('#weatherDescription').append(main + ': ' + description);
                $('#temperature').text(temperature);
                $('#humidity').text(humidity);
                $('#wind').text(wind);
                $('#cityName').text(cityName);
            },
            error: function () {
                $('#errorMessages')
                    .append($('<li>')
                        .attr({ class: 'list-group-item list-group-item-danger' })
                        .text("Error calling web service for today's forecast. Check if your Zipcode is valid."));
                $('#currentWeather').hide();
                $('#fiveDays').hide();
            }
        })


        $.ajax({
            type: 'GET',
            url: 'http://api.openweathermap.org/data/2.5/forecast?zip=' + zip + ',us&units=' + unit + '&appid=c5bf70503882c71db48436245d185d2c',
            success: function (weather) {
                var weatherArray = weather.list;
                var x = 1;

                for (var i = 0; i < weatherArray.length; i += 8) {
                    var strId = 'day' + x;
                    var imageIcon = weatherArray[i].weather[0].icon;
                    var imgUrl = 'http://openweathermap.org/img/w/' + imageIcon + '.png';

                    var date = getDay(weatherArray[i].dt_txt);

                    var row = '<p>' + date + '</p>';
                    row += '<p>' + '<img src=' + imgUrl + '>' + weatherArray[i].weather[0].main + '</p>';
                    row += '<p>H: ' + weatherArray[i].main.temp_max + getUnit() + ' L: ' + weatherArray[i].main.temp_min + getUnit() + '</p>';
                    $('#' + strId).append(row);
                    x++;

                }

            },
            error: function () {
                $('#errorMessages')
                    .append($('<li>')
                        .attr({ class: 'list-group-item list-group-item-danger' })
                        .text('Error calling web service for 5 days forecast. Check if your Zipcode is valid.'));
                $('#currentWeather').hide();
                $('#fiveDays').hide();
            }
        })


    })
}

function initialPage() {
    $('#currentWeather').hide();
    $('#fiveDays').hide();
}

function getDay(x) {
    var arrMonth = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    var date = x.split(' ');
    var arrDate = date[0].split('-')
    var day = arrDate[2];
    var month = arrMonth[(parseInt(arrDate[1]) - 1)];
    return day + ' ' + month;
}

function setUnit() {
    if ($('#unit').val() === 'metric') {
        $('#tempUnit').text('C');
    } else {
        $('#tempUnit').text('F');
    }
}

function getUnit() {
    if ($('#unit').val() === 'metric') {
        return 'C';
    } else {
        return 'F';
    }
}

