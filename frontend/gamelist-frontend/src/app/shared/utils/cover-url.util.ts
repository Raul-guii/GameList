export function getHighResCoverUrl(url: string | undefined): string | undefined {
  if (!url) return undefined;

  let result = url;

  if (result.startsWith('//')) {
    result = 'https:' + result;
  }

  if (result.includes('t_thumb')) {
    result = result.replace('t_thumb', 't_cover_big');
  }

  return result;
}