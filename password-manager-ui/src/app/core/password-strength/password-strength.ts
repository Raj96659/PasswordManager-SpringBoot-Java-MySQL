export function calculateStrength(password: string) {

  let score = 0;

  if (!password) return { score: 0, label: 'Weak', color: 'red' };

  if (password.length >= 8) score += 25;
  if (/[A-Z]/.test(password)) score += 25;
  if (/[0-9]/.test(password)) score += 25;
  if (/[^A-Za-z0-9]/.test(password)) score += 25;

  let label = 'Weak';
  let color = 'red';

  if (score >= 75) {
    label = 'Very Strong';
    color = 'green';
  } else if (score >= 50) {
    label = 'Strong';
    color = 'blue';
  } else if (score >= 25) {
    label = 'Medium';
    color = 'orange';
  }

  return { score, label, color };
}
