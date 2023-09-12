#!/bin/bash
while true; do
    curl -d '{"id":"","name": "Toronja","description":"Fruta del Campo"}' -H "Content-Type: application/json" -X PUT http://localhost:8080/fruits/f2710a3e-ab96-4b68-9ba6-73ed471b89a4
    echo "Saved"
done