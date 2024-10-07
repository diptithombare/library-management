// app.js

document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById('loginForm');
    const bookTable = document.getElementById('bookTable');
    const availableBooksTable = document.getElementById('availableBooksTable');
    
    // User login
    if (loginForm) {
        loginForm.addEventListener('submit', function (e) {
            e.preventDefault();
            const username = document.getElementById('username').value;
            const password = document.getElementById('password').value;

            fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password }),
            })
            .then(response => response.json())
            .then(data => {
                if (data.role === 'LIBRARIAN') {
                    window.location.href = 'librarian.html';
                } else if (data.role === 'MEMBER') {
                    window.location.href = 'member.html';
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }

    // Load books for Librarian
    if (bookTable) {
        fetch('/api/books')
            .then(response => response.json())
            .then(books => {
                const tbody = bookTable.querySelector('tbody');
                books.forEach(book => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.status}</td>
                        <td><button class="btn btn-danger">Delete</button></td>
                    `;
                    tbody.appendChild(row);
                });
            });
    }

    // Load available books for Member
    if (availableBooksTable) {
        fetch('/api/books/available')
            .then(response => response.json())
            .then(books => {
                const tbody = availableBooksTable.querySelector('tbody');
                books.forEach(book => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${book.id}</td>
                        <td>${book.title}</td>
                        <td>${book.author}</td>
                        <td>${book.status}</td>
                        <td><button class="btn btn-primary">Borrow</button></td>
                    `;
                    tbody.appendChild(row);
                });
            });
    }

    // Logout functionality
    const logoutButton = document.getElementById('logoutButton');
    if (logoutButton) {
        logoutButton.addEventListener('click', function () {
            // Clear any session tokens
            window.location.href = 'index.html';
        });
    }
});
