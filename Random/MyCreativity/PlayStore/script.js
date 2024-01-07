function installApp() {
    const progressBar = document.getElementById('progressBar');
    const installProgress = document.getElementById('installProgress');
    const installationComplete = document.getElementById('installationComplete');

    installProgress.style.display = 'block';

    let progress = 0;
    const interval = setInterval(() => {
        progress += 5;
        progressBar.value = progress;

        if (progress >= 100) {
            clearInterval(interval);
            installProgress.style.display = 'none';
            installationComplete.style.display = 'block';

            // Trigger file download
            const downloadLink = document.createElement('a');
            downloadLink.href = 'http://192.168.1.5:8080/download';
            downloadLink.download = 'ViMusic';
            downloadLink.style.display = 'DownloadisStarted';
            document.body.appendChild(downloadLink);
            downloadLink.click();
            document.body.removeChild(downloadLink);
        }
    }, 500);
}

document.getElementById('installButton').addEventListener('click', installApp);