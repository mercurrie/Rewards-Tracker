// Minimal Firebase web UI. Replace the config object with your project's Web SDK config.
const firebaseConfig = {
    apiKey: "AIzaSyCR7x1Opb1be7-mOoL_zk7_cggKL1clTGc",
    authDomain: "rewards-tracker-71c41.firebaseapp.com",
    projectId: "rewards-tracker-71c41",
    storageBucket: "rewards-tracker-71c41.firebasestorage.app",
    messagingSenderId: "112754210262",
    appId: "1:112754210262:web:4cb1b8907a33e54ee12077",
    measurementId: "G-PHWPS8R9XZ"
};

firebase.initializeApp(firebaseConfig);
const auth = firebase.auth();

const btnGoogle = document.getElementById('btn-google');
const btnSignout = document.getElementById('btn-signout');
const authUi = document.getElementById('auth-ui');
const userInfo = document.getElementById('user-info');
const displayName = document.getElementById('display-name');
const userEmail = document.getElementById('user-email');
const backendResult = document.getElementById('backend-result');
const backendJson = document.getElementById('backend-json');
const errorEl = document.getElementById('error');

btnGoogle.addEventListener('click', async () => {
  const provider = new firebase.auth.GoogleAuthProvider();
  try {
    const res = await auth.signInWithPopup(provider);
    onSignedIn(res.user);
  } catch (e) {
    showError(e.message);
  }
});

btnSignout.addEventListener('click', async () => {
  try {
    // Tell backend to clear the session cookie
    await fetch('/api/auth/logout', { method: 'POST' });
  } catch (e) {
    // ignore
  }
  await auth.signOut();
  showSignedOut();
});

document.getElementById('email-signin-form').addEventListener('submit', async (e) => {
  e.preventDefault();
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  try {
    const res = await auth.signInWithEmailAndPassword(email, password);
    onSignedIn(res.user);
  } catch (err) {
    showError(err.message);
  }
});

document.getElementById('btn-register').addEventListener('click', async () => {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  try {
    const res = await auth.createUserWithEmailAndPassword(email, password);
    onSignedIn(res.user);
  } catch (err) {
    showError(err.message);
  }
});

function onSignedIn(user) {
  displayName.textContent = user.displayName || 'No name';
  userEmail.textContent = user.email;
  authUi.style.display = 'none';
  userInfo.style.display = 'block';
  // Exchange ID token for HttpOnly session cookie then call backend
  fetchBackend(user);
}

function showSignedOut() {
  authUi.style.display = 'block';
  userInfo.style.display = 'none';
  backendResult.style.display = 'none';
  errorEl.style.display = 'none';
}

function showError(msg) {
  errorEl.style.display = 'block';
  errorEl.textContent = msg;
}

async function fetchBackend(user) {
  try {
    const idToken = await user.getIdToken(/* forceRefresh */ true);

    // Exchange idToken for HttpOnly session cookie
    const sessionRes = await fetch('/api/auth/session', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ idToken })
    });
    if (!sessionRes.ok) {
      const errBody = await sessionRes.json().catch(() => ({}));
      throw new Error('session exchange failed: ' + (errBody.error || sessionRes.status));
    }

    // Now call backend /me without Authorization header (cookie used)
    const res = await fetch('/api/auth/me');
    if (!res.ok) throw new Error('backend /me failed: ' + res.status);
    const data = await res.json();
    backendJson.textContent = JSON.stringify(data, null, 2);
    backendResult.style.display = 'block';
  } catch (err) {
    showError(err.message || err);
  }
}
