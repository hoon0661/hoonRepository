$(document).ready(function () {
    loadDVDs();
    updateDVD();
    createDVD();
    searchDVDs();
});

function loadDVDs() {
    clearDVDTable();
    var contentRows = $('#contentRows');
    $.ajax({
        type: 'GET',
        url: 'https://tsg-dvds.herokuapp.com/dvds',
        success: function (DVDs) {
            $.each(DVDs, function (index, dvd) {
                var id = dvd.id;
                var title = dvd.title;
                var releaseYear = dvd.releaseYear;
                var director = dvd.director;
                var rating = dvd.rating;
                // var notes = dvd.notes;

                var row = '<tr>';
                row += '<td>' + title + '</td>';
                row += '<td>' + releaseYear + '</td>';
                row += '<td>' + director + '</td>';
                row += '<td>' + rating + '</td>';
                row += '<td><a href="#" onclick="showEditForm(' + id + ')">Edit</a> | <a href="#" onclick="deleteDVD(' + id + ')">Delete</a></td>';

                contentRows.append(row);
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

function searchDVDs() {
    $('#searchButton').click(function (event) {
        $('#errorMessages').empty();
        var category = $('#category').val();
        var input = $('#searchInput').val();
        var path;
        if (category === 'title') {
            path = '/title/';
        } else if (category === 'year') {
            path = '/year/';
        } else if (category === 'director') {
            path = '/director/';
        } else if (category === 'rating') {
            path = '/rating/';
        }

        if (category === 'all') {
            loadDVDs();
        } else {
            loadDVDsByCategory(path, input);
        }
    });

}

function loadDVDsByCategory(path, input) {
    clearDVDTable();
    var contentRows = $('#contentRows');
    $.ajax({
        type: 'GET',
        url: 'https://tsg-dvds.herokuapp.com/dvds' + path + input,
        success: function (DVDs) {
            $.each(DVDs, function (index, dvd) {
                var id = dvd.id;
                var title = dvd.title;
                var releaseYear = dvd.releaseYear;
                var director = dvd.director;
                var rating = dvd.rating;
                // var notes = dvd.notes;

                var row = '<tr>';
                row += '<td>' + title + '</td>';
                row += '<td>' + releaseYear + '</td>';
                row += '<td>' + director + '</td>';
                row += '<td>' + rating + '</td>';
                row += '<td><a href="#" onclick="showEditForm(' + id + ')">Edit</a> | <a href="#" onclick="deleteDVD(' + id + ')">Delete</a></td>';

                contentRows.append(row);
            })
        },
        error: function () {
            $('#errorMessages')
                .append($('<li>')
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text('Both Search Category and Search Term are required.'));
        }
    })
}

function showCreateForm() {
    $('#errorMessagesForCreate').empty();
    $('#initialPage').hide();
    $('#editDVDForm').hide();
    $('#createDVDForm').show();
}



function showEditForm(id) {
    $('#errorMessagesForEdit').empty();

    $('#initialPage').hide();
    $('#createDVDForm').hide();
    $('#editDVDForm').show();
    $.ajax({
        type: 'GET',
        url: 'https://tsg-dvds.herokuapp.com/dvd/' + id,
        success: function (data, status) {
            $('#editTitle').val(data.title);
            $('#editReleaseYear').val(data.releaseYear);
            $('#editDirector').val(data.director);
            $('#editRating').val(data.rating);
            $('#editNotes').val(data.notes);
            $('#editId').val(data.id);
        },
        error: function () {
            $('#errorMessages')
                .append($('<li>')
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text('Error calling web service. Please try again later.'));
        }
    })
}

function updateDVD() {
    $('#saveChanges').click(function (event) {
        var haveValidationErrors = checkAndDisplayValidationErrors($('#editForm').find('input'), '#errorMessagesForEdit');

        if (haveValidationErrors) {
            return false;
        }

        $.ajax({
            type: 'PUT',
            url: 'https://tsg-dvds.herokuapp.com/dvd/' + $('#editId').val(),
            data: JSON.stringify({
                id: $('#editId').val(),
                title: $('#editTitle').val(),
                releaseYear: $('#editReleaseYear').val(),
                director: $('#editDirector').val(),
                rating: $('#editRating').val(),
                notes: $('#editNotes').val()
            }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'dataType': 'json',
            'success': function () {
                $('#errorMessage').empty();
                loadDVDs();
            },
            'error': function () {
                $('errorMessagesForEdit')
                    .append($('<li>')
                        .attr({ class: 'list-group-item list-group-item-danger' })
                        .text('Error calling web service. Please try again later.'));
            }
        })
        backToHome();

    })
}

function createDVD() {
    $('#createButton').click(function (event) {
        var haveValidationErrors = checkAndDisplayValidationErrors($('#DVDForm').find('input'), '#errorMessagesForCreate');
        if (haveValidationErrors) {
            return false;
        }

        $.ajax({
            type: 'POST',
            url: 'https://tsg-dvds.herokuapp.com/dvd',
            data: JSON.stringify({
                title: $('#createTitle').val(),
                releaseYear: $('#createReleaseYear').val(),
                director: $('#createDirector').val(),
                rating: $('#createRating').val(),
                notes: $('#createNotes').val()
            }),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            'dataType': 'json',
            success: function () {
                $('#errorMessage').empty();
                backToHome();
                loadDVDs();
            },
            error: function () {
                $('#errorMessagesForCreate')
                    .append($('<li>')
                        .attr({ class: 'list-group-item list-group-item-danger' })
                        .text('Error calling web service. Please try again later.'));
            }
        })

    })
}

function deleteDVD(id) {
    var ans = confirm("Are you sure you want to delete this DVD from your collection?");
    if (ans == true) {
        $.ajax({
            type: 'DELETE',
            url: 'https://tsg-dvds.herokuapp.com/dvd/' + id,
            success: function () {
                loadDVDs();
            }
        });
    } else {
        return;
    }
}

function checkAndDisplayValidationErrors(input, formId) {
    $(formId).empty();

    var errorMessages = [];

    input.each(function () {
        if (!this.validity.valid) {
            var errorField = $('label[for=' + this.id + ']').text();
            errorMessages.push(errorField + ' ' + this.validationMessage);
        }
    });

    if (errorMessages.length > 0) {
        $.each(errorMessages, function (index, message) {
            $(formId).append($('<li>').attr({ class: 'list-group-item list-group-item-danger' }).text(message));
        });
        // return true, indicating that there were errors
        return true;
    } else {
        // return false, indicating that there were no errors
        return false;
    }
}

function backToHome() {
    $('#errorMessagesForEdit').empty();
    $('#errorMessagesForCreate').empty();
    $('#errorMessages').empty();

    $('#editTitle').val('');
    $('#editReleaseYear').val('');
    $('#editDirector').val('');
    $('#editRating').val('');
    $('#editNotes').val('');
    $('#editId').val('');
    $('#editDVDForm').hide();

    $('#createTitle').val('');
    $('#createReleaseYear').val('');
    $('#createDirector').val('');
    $('#createRating').val('');
    $('#createNotes').val('');
    $('#editId').val('');
    $('#createDVDForm').hide();

    $('#initialPage').show();
}

function clearDVDTable() {
    $('#contentRows').empty();
}


