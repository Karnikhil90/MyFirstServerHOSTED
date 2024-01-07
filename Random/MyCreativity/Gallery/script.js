const imageFolder = 'C:\\Users\\USER\\Desktop\\imageFolder'; // Replace with the path to your image folder
const imageGallery = document.getElementById('imageGallery');

fetchImages(imageFolder);

function fetchImages(folder) {
    fetch(folder)
        .then(response => response.text())
        .then(data => {
            const imageFiles = data.split('\n').filter(fileName => fileName.trim() !== '');

            imageFiles.forEach(fileName => {
                const imageUrl = `${folder}/${fileName}`;
                createImageElement(imageUrl);
            });
        })
        .catch(error => console.error('Error fetching images:', error));
}

function createImageElement(imageUrl) {
    const imageContainer = document.createElement('div');
    imageContainer.classList.add('image-container');

    const image = document.createElement('img');
    image.src = imageUrl;
    image.alt = imageUrl;

    imageContainer.appendChild(image);
    imageGallery.appendChild(imageContainer);
}